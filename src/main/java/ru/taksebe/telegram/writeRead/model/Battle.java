package ru.taksebe.telegram.writeRead.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

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

    Long time;

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