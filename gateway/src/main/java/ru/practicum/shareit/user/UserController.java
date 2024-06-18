package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdatedDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto newUser) {
        log.info("Создание пользователя: {}", newUser);
        return userClient.createUser(newUser);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получение пользователей");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(name = "userId") Long userId) {
        log.info("Получение пользователей");
        return userClient.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(name = "userId") Long userId,
                                             @Valid @RequestBody UserUpdatedDto user) {
        log.info("Обновление пользователя с id: {}", userId);
        return userClient.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable(name = "userId") Long userId) {
        log.info("Удаление пользователя с id: {}", userId);
        return userClient.deleteUserById(userId);
    }
}