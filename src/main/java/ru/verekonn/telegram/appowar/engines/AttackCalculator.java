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
        //ax
        var attack = attackUser.getAttackPower();
        //dy
        var defence = underAttackUser.getDefencePower();
        var isDefenced = underAttackUserAction.getCurrent().getValue()
                .equals(UserAction.DEFENSE);
        //dy'
        var defenceUnderAttackUser = isDefenced ?
                defence * underAttackUser.getDefenceCoefficient() : defence;
        //ay
        var contrAttackUnderAttackUser = attack
                * underAttackUser.getContrAttackCoefficient();
        // TODO: CHECK
        var i = random.nextInt();
        boolean win = i % 2 == 0;
        if (win) {
            return AttackResult.WIN;
        } else {
            return AttackResult.DEFENDED;
        }
    }
}
