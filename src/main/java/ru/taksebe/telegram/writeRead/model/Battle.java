package ru.taksebe.telegram.writeRead.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("battle")
public class Battle {

    /**
     * Словарное слово
     */
    @Id
    String id;

    List<UserBattleState> user;

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