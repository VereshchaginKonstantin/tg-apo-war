package ru.taksebe.telegram.writeRead.model.converters;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import ru.taksebe.telegram.writeRead.model.UserBattleState;

public class UserBattleStateToBytesConverter implements Converter<UserBattleState, byte[]> {
    private final Jackson2JsonRedisSerializer<UserBattleState> serializer;

    public UserBattleStateToBytesConverter() {
        serializer = new Jackson2JsonRedisSerializer<>(UserBattleState.class);
        serializer.setObjectMapper(new ObjectMapper());
    }

    @Override
    public byte[] convert(@Nullable UserBattleState value) {
        return serializer.serialize(value);
    }
}