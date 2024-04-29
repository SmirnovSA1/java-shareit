package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userStorage = new HashMap<>();
    private long generatedId = 0L;

    @Override
    public User createUser(User newUser) {
        newUser.setId(generateId());
        userStorage.put(newUser.getId(), newUser);
        log.info(String.format("Пользователь %s успешно создан", newUser));
        return newUser;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получен список пользователь");
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public User getUserById(Long userId) {
        log.info("Получен пользователь по id: {}", userId);
        return userStorage.get(userId);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User foundUser = getUserById(userId);
        user = User.builder()
                .id(userId)
                .name(user.getName() != null ? user.getName() : foundUser.getName())
                .email(user.getEmail() != null ? user.getEmail() : foundUser.getEmail())
                .build();
        userStorage.put(user.getId(), user);
        log.info(String.format("Пользователь %s успешно обновлен", user));
        return user;
    }

    @Override
    public Map<String, String> deleteUserById(Long userId) {
        userStorage.remove(userId);
        log.info(String.format("Пользователь %s успешно удален", userId));
        return Map.of("message", String.format("Пользователь с id {} удален", userId));
    }

    private Long generateId() {
        return ++generatedId;
    }
}
