package ru.taksebe.telegram.writeRead.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@RedisHash("user")
@Builder
public class UserBattleState {
    Long attack;
    Long defence;
}