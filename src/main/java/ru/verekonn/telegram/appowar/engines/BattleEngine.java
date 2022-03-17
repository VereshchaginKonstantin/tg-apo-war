package ru.verekonn.telegram.appowar.engines;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.verekonn.telegram.appowar.model.Battle;
import ru.verekonn.telegram.appowar.model.BattleState;
import ru.verekonn.telegram.appowar.model.repository.BattleRepository;
import ru.verekonn.telegram.appowar.utils.HistoryItem;

@RequiredArgsConstructor
@Component
public class BattleEngine {

    static Random random = new Random(2333);

    BattleRepository battleRepository;




    public List<Battle> step() {
        List<Battle> result = new ArrayList<>();
        proceedAll(b -> {
            result.add(b);
            if (b.getState().timeSpend() > Battle.END) {
                b.getState().addValue(BattleState.END);
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
        b.setTimestamp(new Date());
        battleRepository.save(b);
    }

    private void proceedAll(Consumer<Battle> action) {
        battleRepository.findAll().forEach(action);
    }

    private void touch(Battle b) {
        b.setTimestamp(new Date());
        battleRepository.save(b);
    }

    public void clean() {
        proceedAll(x -> {
            if (x.getState().equals(BattleState.END)) {
                battleRepository.delete(x);
            }
        });
    }
}
