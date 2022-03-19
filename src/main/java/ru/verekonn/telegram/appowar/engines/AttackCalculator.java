package ru.verekonn.telegram.appowar.engines;

import java.util.Random;

import ru.verekonn.telegram.appowar.model.User;
import ru.verekonn.telegram.appowar.model.UserAction;
import ru.verekonn.telegram.appowar.utils.HistoryItem;
import ru.verekonn.telegram.appowar.utils.HistoryList;

public class AttackCalculator {

    static Random random = new Random(9000);


    public static boolean checkPercent(double x) {
        var i = random.nextInt();
        var rest = i % 100;
        return rest < x;
    }

    public static AttackResult howWin(
            User attackUser,
            User underAttackUser,
            HistoryList<HistoryItem<UserAction>> underAttackUserAction) {
        // ax
        var attack = attackUser.getAttackPower();
        // dy
        var defence = underAttackUser.getDefencePower();
        var isDefenced = underAttackUserAction.getCurrent().getValue()
                .equals(UserAction.DEFENSE);
        // dy'
        var defenceUnderAttackUser = isDefenced ?
                defence * underAttackUser.getDefenceCoefficient() : defence;
        // ay
        var contrAttackUnderAttackUser = attack
                * underAttackUser.getContrAttackCoefficient();
        var attackPercent = 100L * attack / (defenceUnderAttackUser + attack);
        var contrAttackPercent = 100L * contrAttackUnderAttackUser /
                (contrAttackUnderAttackUser + attackUser.getDefencePower());
        if (checkPercent(attackPercent)) {
            return AttackResult.WIN;
        } else if (checkPercent(contrAttackPercent)) {
            return AttackResult.LOOSE;
        } else {
            return AttackResult.DEFENDED;
        }
    }
}
