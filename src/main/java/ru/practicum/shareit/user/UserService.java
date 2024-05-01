package ru.practicum.shareit.user;

import java.util.List;
import java.util.Map;

public interface UserService {

    User createUser(User newUser);

    List<User> getAllUsers();

    User getUserById(Long userId);

    User updateUser(Long userId, User user);

    Map<String, String> deleteUserById(Long userId);
}
