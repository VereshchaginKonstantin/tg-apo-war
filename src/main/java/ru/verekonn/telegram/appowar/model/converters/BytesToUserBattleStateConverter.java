package ru.verekonn.telegram.appowar.model.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import ru.verekonn.telegram.appowar.model.UserBattleState;

import javax.annotation.Nullable;

public class BytesToUserBattleStateConverter implements Converter<byte[], UserBattleState> {
    private final Jackson2JsonRedisSerializer<UserBattleState> serializer;

    public BytesToUserBattleStateConverter() {
        serializer = new Jackson2JsonRedisSerializer<>(UserBattleState.class);
        serializer.setObjectMapper(new ObjectMapper());
    }

    @Override
    public UserBattleState convert(@Nullable byte[] value) {
        return serializer.deserialize(value);
    }
}