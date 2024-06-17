package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.response.Response;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toUserDtoList(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userMapper.toUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId))));
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User userFromDto = userMapper.toUser(userDto);
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));

        if (userFromDto.getName() != null) {
            foundUser.setName(userFromDto.getName());
        }

        if (userFromDto.getEmail() != null) {
            foundUser.setEmail(userFromDto.getEmail());
        }

        return userMapper.toUserDto(userRepository.save(foundUser));
    }

    @Transactional
    @Override
    public ResponseEntity<Response> deleteUserById(Long userId) {
        userRepository.deleteById(userId);
        return new ResponseEntity<>(
                new Response("Успех", String.format("Пользователь с id %d удален", userId)), HttpStatus.OK);
    }
}
