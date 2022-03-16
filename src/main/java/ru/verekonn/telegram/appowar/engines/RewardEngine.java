package ru.verekonn.telegram.appowar.engines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.verekonn.telegram.appowar.model.Battle;
import ru.verekonn.telegram.appowar.model.BattleState;
import ru.verekonn.telegram.appowar.model.repository.BattleRepository;
import ru.verekonn.telegram.appowar.model.repository.UserRepository;

@RequiredArgsConstructor
@Component
public class RewardEngine {
    UserRepository userRepository;

    public void proceedReward(List<Battle> procceded) {
        procceded.stream().filter(
                x
                ->
                x.getState().equals(BattleState.END))
                .forEach(b -> {
                    var userFirst = userRepository
                            .findById(b
                                    .getWinnerUserName())
                            .get();
                    var userSecond = userRepository
                            .findById(b
                                    .getLooserUserName())
                            .get();
                    userFirst.setWins(userFirst.getWins() + 1);
                    userFirst.setCash(userFirst.getCash() + 100);
                    userSecond.setCash(userSecond.getCash() - 50);
                    userSecond.setLoose(userSecond.getLoose() + 1);
                });
    }
}
