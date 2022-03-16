package ru.taksebe.telegram.writeRead.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import ru.taksebe.telegram.writeRead.model.User;

import javax.annotation.Nullable;

public class BytesToUserConverter implements Converter<byte[], User> {
    private final Jackson2JsonRedisSerializer<User> serializer;

    public BytesToUserConverter() {
        serializer = new Jackson2JsonRedisSerializer<>(User.class);
        serializer.setObjectMapper(new ObjectMapper());
    }

    @Override
    public User convert(@Nullable byte[] value) {
        return serializer.deserialize(value);
    }
}