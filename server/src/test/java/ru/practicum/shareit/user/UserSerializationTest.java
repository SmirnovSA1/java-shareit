package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class UserSerializationTest {
    @Autowired
    private JacksonTester<UserDto> toJsonTester;
    @Autowired
    private JacksonTester<UserDto> toDtoTester;

    @Test
    void dtoSerializationTest() throws IOException {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("User")
                .email("userEmail@test.com")
                .build();

        JsonContent<UserDto> result = toJsonTester.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("User");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("userEmail@test.com");
    }

    @Test
    void dtoDeserializationTest() throws IOException {
        String json = "{ \"name\":\"User\", \"email\":\"userEmail@test.com\"}";
        UserDto userDto = UserDto.builder()
                .name("User")
                .email("userEmail@test.com")
                .build();

        UserDto result = toDtoTester.parseObject(json);

        assertEquals(userDto, result);
    }
}
