package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
    public User createUser(@Valid @RequestBody User newUser) {
        log.info("Создание пользователя: {}", newUser);
        return ResponseEntity.ok().body(userService.createUser(newUser)).getBody();
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получение пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable(name = "userId") Long userId) {
        log.info("Получение пользователей");
        return ResponseEntity.ok().body(userService.getUserById(userId)).getBody();
    }

    @PatchMapping("/{userId}")
    public User updateUser(@Valid @PathVariable(name = "userId") Long userId,
                           @RequestBody User user) {
        log.info("Обновление пользователя с id: {}", userId);
        return ResponseEntity.ok().body(userService.updateUser(userId, user)).getBody();
    }

    @DeleteMapping("/{userId}")
    public Map<String, String> deleteUserById(@PathVariable(name = "userId") Long userId) {
        log.info("Удаление пользователя с id: {}", userId);
        Map<String, String> result = userService.deleteUserById(userId);
        return ResponseEntity.ok().body(result).getBody();
    }
}
