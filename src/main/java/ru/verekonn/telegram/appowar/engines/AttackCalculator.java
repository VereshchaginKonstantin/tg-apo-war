package ru.verekonn.telegram.appowar.engines;

import java.util.Random;

import ru.verekonn.telegram.appowar.model.User;
import ru.verekonn.telegram.appowar.model.UserAction;
import ru.verekonn.telegram.appowar.utils.HistoryItem;
import ru.verekonn.telegram.appowar.utils.HistoryList;

public class AttackCalculator {

    static Random random = new Random(2333);

    public static AttackResult howWin(
            User attackUser,
            User underAttackUser,
            HistoryList<HistoryItem<UserAction>> underAttackUserAction) {
        var i = random.nextInt();
        boolean win = i % 2 == 0;
        if (win) {
            return AttackResult.WIN;
        } else {
            return AttackResult.DEFENDED;
        }
    }
}
