package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepository;

    @Override
    public User createUser(User newUser) {
        userEmailExist(null, newUser);
        return userRepository.createUser(newUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(Long userId) {
        User foundUser = userRepository.getUserById(userId);

        if (foundUser == null) {
            log.info(String.format("Не найден пользователь c id %d", userId));
            throw new NotFoundException(String.format("Не найден пользователь c id %d", userId));
        }

        return foundUser;
    }

    @Override
    public User updateUser(Long userId, User user) {
        userEmailExist(userId, user);
        getUserById(userId);
        return userRepository.updateUser(userId, user);
    }

    @Override
    public Map<String, String> deleteUserById(Long userId) {
        getUserById(userId);
        return userRepository.deleteUserById(userId);
    }

    public void userEmailExist(Long userId, User user) throws AlreadyExistException {
        if (user.getEmail() == null) return;

        String checkedEmail = user.getEmail();
        boolean foundEmail = userRepository.getAllUsers().stream()
                .anyMatch(user1 -> user1.getEmail().equals(checkedEmail) && !user1.getId().equals(userId));

        if (foundEmail) throw new AlreadyExistException("Пользователь с таким email уже существует");
    }
}
