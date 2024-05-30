package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.response.Response;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@ResponseBody
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserDto newUser) {
        log.info("Создание пользователя: {}", newUser);
        return ResponseEntity.ok().body(userService.createUser(newUser)).getBody();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        log.info("Получение пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable(name = "userId") Long userId) {
        log.info("Получение пользователей");
        return ResponseEntity.ok().body(userService.getUserById(userId)).getBody();
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@Valid @PathVariable(name = "userId") Long userId,
                              @RequestBody UserDto user) {
        log.info("Обновление пользователя с id: {}", userId);
        return ResponseEntity.ok().body(userService.updateUser(userId, user)).getBody();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response> deleteUserById(@PathVariable(name = "userId") Long userId) {
        log.info("Удаление пользователя с id: {}", userId);
        return userService.deleteUserById(userId);
    }
}
