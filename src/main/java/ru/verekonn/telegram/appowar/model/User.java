package ru.verekonn.telegram.appowar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter
@Setter
@RedisHash("user")
@Builder
@ToString
public class User {

    /**
     * Идентификатор - для пользовательских словарей это id чата с пользователем в Telegram, для предзагруженных -
     * элементы перечисления путей до словарей по умочанию (пакет constants)
     */
    @Id
    String userName;

    String chatId;

    Long attackPower;

    Long defencePower;

    Long speedDefenceMs;

    Long speedAttackMs;

    Long cash;

    Long wins;

    Long loose;
}