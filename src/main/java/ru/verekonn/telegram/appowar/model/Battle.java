package ru.verekonn.telegram.appowar.model;

import java.util.Date;

import javax.validation.constraints.Null;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import ru.verekonn.telegram.appowar.utils.HistoryItem;
import ru.verekonn.telegram.appowar.utils.HistoryList;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@RedisHash("battle")
@ToString
public class Battle {

    static public int END = 5;

    /**
     * Словарное слово
     */
    @Id
    String id;

    HistoryList<HistoryItem<BattleState>> state;

    Date timestamp;

    String winnerUserName;

    String looserUserName;

    UserBattleState userFirst;

    UserBattleState userSecond;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return this.id.equals(((Battle) obj).getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public String getOtherUser(String userName) {
        if (userFirst.getUserName().equals(userName)) {
            return userSecond.getUserName();
        } else {
            return userFirst.getUserName();
        }
    }

    public String getAttackUser() {
        var u1 = userFirst
                .getAction()
                .getCurrent();
        var u2 = userFirst
                .getAction()
                .getCurrent();
        if (u1.getValue().equals(UserAction.ATTACK) &&
                !u2.getValue().equals(UserAction.ATTACK)
        ) {
            return userFirst.getUserName();
        }
        if (!u1.getValue().equals(UserAction.ATTACK) &&
                u2.getValue().equals(UserAction.ATTACK)
        ) {
            return userFirst.getUserName();
        }
        if (u1.getValue().equals(UserAction.ATTACK) &&
                u2.getValue().equals(UserAction.ATTACK)
        ) {
            if (u1.getTimestamp().before(u2.getTimestamp())) {
                return userFirst.getUserName();
            } else {
                return userSecond.getUserName();
            }
        }
        return null;
    }

    public UserBattleState getUser(String userName) {
        if (userFirst.getUserName().equals(userName)) {
            return userFirst;
        } else {
            return userSecond;
        }
    }

}