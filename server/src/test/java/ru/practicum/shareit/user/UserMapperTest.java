package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void toUserDto() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("email@test.ru")
                .build();

        UserDto expected = UserDto.builder()
                .id(1L)
                .name(user.getName())
                .email(user.getEmail())
                .build();

        UserDto result = userMapper.toUserDto(user);

        assertEquals(expected, result);
    }

    @Test
    void toUser() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("email@test.ru")
                .build();

        User expected = User.builder()
                .id(1L)
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();

        User result = userMapper.toUser(userDto);

        assertEquals(expected, result);
    }

    @Test
    void toUserDtoList() {
        User user1 = User.builder()
                .id(1L)
                .name("name 1")
                .email("email1@test.ru")
                .build();

        User user2 = User.builder()
                .id(2L)
                .name("name 2")
                .email("email2@test.ru")
                .build();

        List<User> userList = List.of(user1, user2);

        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .name("name 1")
                .email("email1@test.ru")
                .build();

        UserDto userDto2 = UserDto.builder()
                .id(2L)
                .name("name 2")
                .email("email2@test.ru")
                .build();

        List<UserDto> userDtoList = List.of(userDto1, userDto2);

        List<UserDto> result = userMapper.toUserDtoList(userList);

        assertEquals(userDtoList, result);
    }
}
