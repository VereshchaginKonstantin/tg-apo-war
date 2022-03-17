package ru.verekonn.telegram.appowar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import ru.verekonn.telegram.appowar.utils.HistoryList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("userbattlestate")
@Builder
@ToString
public class UserBattleState {
    String userName;
    HistoryList<Long> attack;
    HistoryList<Long> defence;
}