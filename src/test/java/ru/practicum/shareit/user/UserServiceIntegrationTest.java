package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class UserServiceIntegrationTest {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;

    @Test
    void saveUser() {
        UserDto userDto = UserDto.builder().name("Test").email("testEmail@test.com").build();

        UserDto savedUser = userService.createUser(userDto);

        assertNotNull(savedUser.getId());
        assertEquals(1L, savedUser.getId());
        assertEquals(userDto.getName(), savedUser.getName());
        assertEquals(userDto.getEmail(), savedUser.getEmail());

        Optional<User> userFromDb = userRepository.findById(savedUser.getId());
        assertTrue(userFromDb.isPresent());
        assertEquals(userDto.getName(), userFromDb.get().getName());
        assertEquals(userDto.getEmail(), userFromDb.get().getEmail());
    }
}
