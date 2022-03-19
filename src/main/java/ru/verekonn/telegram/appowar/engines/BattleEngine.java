package ru.verekonn.telegram.appowar.engines;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import ru.verekonn.telegram.appowar.model.repository.UserRepository;
import ru.verekonn.telegram.appowar.utils.HistoryItem;
import ru.verekonn.telegram.appowar.utils.HistoryList;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
public class BattleEngine {

    BattleRepository battleRepository;
    UserRepository userRepository;

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
                .getCurrent()
                .getValue()
                .equals(UserAction.ATTACK) ||
                b
                .getUserSecond()
                .getAction()
                .getCurrent()
                .getValue()
                .equals(UserAction.ATTACK)) {
            attack(b);
        }
        if (b
                .getUserFirst()
                .getAction()
                .timeSpendAfterLastChanges() > Battle.END_MS &&
                b
                .getUserSecond()
                .getAction()
                .timeSpendAfterLastChanges() > Battle.END_MS
        ) {
            b.getState().add(new HistoryItem<>(BattleState.DRAW_BY_TIME));
            return;
        }
        if (b.getState().getCurrent().getValue().equals(BattleState.INIT)) {
            b.getState().add(new HistoryItem<>(BattleState.PROCESS));
            return;
        }
    }

    private void recalculateStates(Battle b) {
        var dateNow = new Date();
        var userState = b.getUserFirst();
        nextAttack(dateNow, userState);
        nextDefence(dateNow, userState);
        userState = b.getUserSecond();
        nextAttack(dateNow, userState);
        nextDefence(dateNow, userState);
        battleRepository.save(b);
    }

    private void nextDefence(Date dateNow, UserBattleState userState) {
        var user = userRepository
                .findById(userState.getUserName())
                .get();
        var current = userState.getAction()
                .getCurrent();
        if (current.getValue()
                .equals(UserAction.PREPARE_DEFENSE)) {
            var changeDate = current.getTimestamp();
            var nextdate = new Date(changeDate.getTime() + user.getSpeedDefenceMs());
            if (nextdate.before(dateNow)) {
                userState.getAction().add(new HistoryItem<>(UserAction.DEFENSE, nextdate));
            }
        }
    }

    private void nextAttack(Date dateNow, UserBattleState userState) {
        var user = userRepository
                .findById(userState.getUserName())
                .get();
        var current = userState.getAction()
                .getCurrent();
        if (current.getValue()
                .equals(UserAction.PREPARE_ATTACK)) {
            var changeDate = current.getTimestamp();
            var nextdate = new Date(changeDate.getTime() + user.getSpeedAttackMs());
            if (nextdate.before(dateNow)) {
                userState.getAction().add(new HistoryItem<>(UserAction.ATTACK, nextdate));
            }
        }
    }

    private void attack(Battle b) {
        var attackUserName = b.getAttackUser();
        var underAttackUserName = b.getOtherUser(attackUserName);
        if (attackUserName != null) {
            var attackUser = userRepository
                    .findById(attackUserName)
                    .get();
            var underAttackUser = userRepository
                    .findById(underAttackUserName)
                    .get();

            var result = AttackCalculator.howWin(attackUser,
                    underAttackUser,
                    b.getUser(underAttackUserName).getAction(),
                    b.getUser(attackUserName).getAction());
            if (result.equals(AttackResult.WIN)) {
                b.getUser(attackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.ATTACK_SUCCESS));
                b.getUser(underAttackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.DEFENSE_FAILED));
                b.getUser(underAttackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.KILLED));
                b.setWinnerUserName(attackUserName);
                b.setLooserUserName(underAttackUserName);
                b.getState().add(new HistoryItem<>(BattleState.END));
            }
            if (result.equals(AttackResult.LOOSE)) {
                b.getUser(attackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.ATTACK_FAILED));
                b.getUser(attackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.DEFENSE_FAILED));
                b.getUser(attackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.KILLED));
                b.getUser(underAttackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.DEFENSE_SUCCESS));
                b.getUser(underAttackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.CONTR_ATTACK));
                b.getUser(underAttackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.ATTACK_SUCCESS));
                b.setWinnerUserName(underAttackUserName);
                b.setLooserUserName(attackUserName);
                b.getState().add(new HistoryItem<>(BattleState.END));
            }
            if (result.equals(AttackResult.DEFENDED)) {
                b.getUser(attackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.ATTACK_FAILED));
                b.getUser(attackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.DEFENSE_SUCCESS));
                b.getUser(underAttackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.DEFENSE_SUCCESS));
                b.getUser(underAttackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.CONTR_ATTACK));
                b.getUser(underAttackUserName).getAction()
                        .add(new HistoryItem<>(UserAction.ATTACK_FAILED));
                b.getUser(attackUserName)
                        .getAction()
                        .add(new HistoryItem<>(UserAction.PREPARE_DEFENSE));
                b.getUser(underAttackUserName)
                        .getAction()
                        .add(new HistoryItem<>(UserAction.PREPARE_DEFENSE));
            }
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
