package ru.practicum.shareit.user.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.response.Response;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto newUser);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto updateUser(Long userId, UserDto user);

    ResponseEntity<Response> deleteUserById(Long userId);
}
