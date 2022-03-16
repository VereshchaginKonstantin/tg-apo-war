package ru.taksebe.telegram.writeRead.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@RedisHash("user")
@Builder
public class User {

    /**
     * Идентификатор - для пользовательских словарей это id чата с пользователем в Telegram, для предзагруженных -
     * элементы перечисления путей до словарей по умочанию (пакет constants)
     */
    @Id
    String userName;

    Long cash;

    Long wins;

    Long loose;
}