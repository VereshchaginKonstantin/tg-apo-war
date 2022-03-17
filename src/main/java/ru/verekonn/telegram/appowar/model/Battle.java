package ru.verekonn.telegram.appowar.model;

import java.util.Date;

import javax.validation.constraints.Null;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import ru.verekonn.telegram.appowar.utils.HistoryList;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@RedisHash("battle")
@ToString
public class Battle {

    static public int END = 3;

    /**
     * Словарное слово
     */
    @Id
    String id;

    HistoryList<BattleState> state;

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
}