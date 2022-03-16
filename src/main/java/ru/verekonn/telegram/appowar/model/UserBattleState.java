package ru.verekonn.telegram.appowar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("user")
@Builder
@ToString
public class UserBattleState {
    String userName;
    Long attack;
    Long defence;
}