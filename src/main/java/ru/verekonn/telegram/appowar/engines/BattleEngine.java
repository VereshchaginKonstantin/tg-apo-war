package ru.verekonn.telegram.appowar.engines;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.verekonn.telegram.appowar.model.Battle;
import ru.verekonn.telegram.appowar.model.BattleState;
import ru.verekonn.telegram.appowar.model.repository.BattleRepository;
import ru.verekonn.telegram.appowar.utils.HistoryItem;

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
            if (b.getState().timeSpend() > Battle.END) {
                b.getState().add(new HistoryItem<>(BattleState.DRAW));
            }
            if (false) {
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
        });
        return result;
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
}
