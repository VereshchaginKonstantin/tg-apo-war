package ru.verekonn.telegram.appowar.engines;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.hpsf.GUID;
import org.springframework.stereotype.Component;
import ru.verekonn.telegram.appowar.model.Battle;
import ru.verekonn.telegram.appowar.model.BattleState;
import ru.verekonn.telegram.appowar.model.User;
import ru.verekonn.telegram.appowar.model.UserAction;
import ru.verekonn.telegram.appowar.model.UserBattleState;
import ru.verekonn.telegram.appowar.model.repository.BattleRepository;
import ru.verekonn.telegram.appowar.utils.HistoryItem;
import ru.verekonn.telegram.appowar.utils.HistoryList;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
public class BattleEngine {

    BattleRepository battleRepository;

    static Random random = new Random(2333);


    public void touch(List<Battle> procceded) {
        procceded.forEach(this::touch);
    }

    public void touch(Battle b) {
        b.setTimestamp(new Date());
        save(b);
    }

    public List<Battle> step() {
        List<Battle> result = new ArrayList<>();
        proceedAll(b -> {
            result.add(b);
            step(b);
        });
        return result;
    }

    private void step(Battle b) {
        recalculateStates(b);
        if (b
                .getUserFirst()
                .getAction()
                .equals(UserAction.ATTACK) ||
                b
                .getUserSecond()
                .getAction()
                .equals(UserAction.ATTACK)) {
            attack(b);
        }
        if (b.getState().timeSpendAfterLastChanges() > Battle.END) {
            b.getState().add(new HistoryItem<>(BattleState.DRAW_BY_TIME));
            return;
        }
        if (b.getState().getCurrent().getValue().equals(BattleState.INIT)) {
            b.getState().add(new HistoryItem<>(BattleState.PROCESS));
            return;
        }
    }

    private void recalculateStates(Battle b) {
        //todo: Сделать изменение состояний в степе на основе скорости
        // а не текущего времени, то-есть будет дата начала плюс скорость если
    }

    private void attack(Battle b) {
        b.getState().add(new HistoryItem<>(BattleState.END));
        var i = random.nextInt();
        boolean win = i % 2 == 0;
        if (win) {
            b.setWinnerUserName(
                    b
                            .getUserFirst()
                            .getUserName());
            b.setLooserUserName(
                    b
                            .getUserSecond()
                            .getUserName());
        } else {
            b.setWinnerUserName(
                    b
                            .getUserSecond()
                            .getUserName());
            b.setLooserUserName(
                    b
                            .getUserFirst()
                            .getUserName());
        }
        save(b);
    }

    private void save(Battle b) {
        battleRepository.save(b);
    }

    private void proceedAll(Consumer<Battle> action) {
        battleRepository.findAll().forEach(action);
    }

    public void clean() {
        proceedAll(x -> {
            if (x.getState()
                    .getCurrent()
                    .getValue()
                    .IsFinal()) {
                battleRepository.delete(x);
            }
        });
    }

    public boolean attack(String battleId, String userName) {
        var battle = battleRepository.findById(battleId);
        if (battle.isPresent()) {
            var user = battle.get().getUser(userName);
            user.getAction().add(new HistoryItem<>(UserAction.PREPARE_ATTACK));
            battleRepository.save(battle.get());
            return true;

        }
        return false;
    }

    public boolean defence(String battleId, String userName) {
        var battle = battleRepository.findById(battleId);
        if (battle.isPresent()) {
            var user = battle.get().getUser(userName);
            user.getAction().add(new HistoryItem<>(UserAction.PREPARE_DEFENSE));
            battleRepository.save(battle.get());
            return true;

        }
        return false;
    }

    public void createBattle(User user, User userSecond) {
            battleRepository.save(
                    new Battle(
                            (new GUID()).toString(),
                            new HistoryList<>(new HistoryItem<>(BattleState.INIT)),
                            new Date(),
                            "",
                            "",
                            new UserBattleState(
                                    user.getUserName(),
                                    new HistoryList<>(new HistoryItem<>(UserAction.DEFENSE))),
                            new UserBattleState(
                                    userSecond.getUserName(),
                                    new HistoryList<>(new HistoryItem<>(UserAction.DEFENSE)))
                                    ));
    }
}
