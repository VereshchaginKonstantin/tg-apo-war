package ru.taksebe.telegram.writeRead.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import ru.taksebe.telegram.writeRead.model.User;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Nullable;

public class UserToBytesConverter implements Converter<User, byte[]> {
    private final Jackson2JsonRedisSerializer<User> serializer;

    public UserToBytesConverter() {
        serializer = new Jackson2JsonRedisSerializer<>(User.class);
        serializer.setObjectMapper(new ObjectMapper());
    }

    @Override
    public byte[] convert(@Nullable User value) {
        return serializer.serialize(value);
    }
}