package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.response.Response;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;
    private User savedUserForCheck;
    private User updatedUserForCheck;
    private UserDto userDto;
    private UserDto updatedUserDto;

    @BeforeEach
    void setUp() {
        savedUserForCheck = new User(1L, "User", "user@test.com");
        updatedUserForCheck = new User(1L, "New Name", "user@test.com");
        userDto = createNewUserDto();
        updatedUserDto = createUserDtoForUpdate();
    }

    @Test
    void createUser() {
        when(userMapper.toUser(userDto)).thenReturn(new User(null, "User", "user@test.com"));
        when(userRepository.save(any(User.class))).thenReturn(savedUserForCheck);
        when(userMapper.toUserDto(any(User.class))).thenReturn(UserDto.builder().id(1L).name("User").email("user@test.com").build());

        UserDto savedUserDto = userService.createUser(userDto);

        assertNotNull(savedUserDto);
        assertEquals(userDto.getName(), savedUserDto.getName());
        assertEquals(userDto.getEmail(), savedUserDto.getEmail());
        assertEquals(1L, savedUserDto.getId());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void getAllUsers() {
        userDto.setId(1L);

        List<User> userListForCheck = List.of(savedUserForCheck, updatedUserForCheck);
        List<UserDto> userDtoListForCheck = List.of(userDto, updatedUserDto);

        when(userRepository.findAll()).thenReturn(userListForCheck);
        when(userMapper.toUserDtoList(Mockito.anyList())).thenReturn(userDtoListForCheck);

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(userDto.getId(), result.get(0).getId());
        assertEquals(updatedUserDto.getId(), result.get(1).getId());
        assertEquals(userDto.getName(), result.get(0).getName());
        assertEquals(updatedUserDto.getName(), result.get(1).getName());
        assertEquals(userDto.getEmail(), result.get(0).getEmail());
        assertEquals(updatedUserDto.getEmail(), result.get(1).getEmail());
    }

    @Test
    void getUserById() {
        userDto.setId(1L);

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(savedUserForCheck));
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        UserDto foundUserDto = userService.getUserById(1L);

        assertNotNull(foundUserDto);
        assertEquals(userDto.getId(), foundUserDto.getId());
        assertEquals(userDto.getName(), foundUserDto.getName());
        assertEquals(userDto.getEmail(), foundUserDto.getEmail());
    }

    @Test
    void updateUser() {
        when(userMapper.toUser(updatedUserDto)).thenReturn(new User(1L, "New Name", "user@test.com"));
        when(userRepository.save(any(User.class))).thenReturn(updatedUserForCheck);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(updatedUserForCheck));
        when(userMapper.toUserDto(any(User.class))).thenReturn(UserDto.builder().id(1L).name("New Name").email("user@test.com").build());

        UserDto result = userService.updateUser(1L, updatedUserDto);

        assertNotNull(result);
        assertEquals(updatedUserForCheck.getId(), result.getId());
        assertEquals(updatedUserForCheck.getName(), result.getName());
        assertEquals(updatedUserForCheck.getEmail(), result.getEmail());
    }

    @Test
    void deleteUserById() {
        doNothing().when(userRepository).deleteById(any(Long.class));

        ResponseEntity<Response> result = userService.deleteUserById(savedUserForCheck.getId());

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Успех", result.getBody().getTitle());
        assertEquals("Пользователь с id 1 удален", result.getBody().getDescription());
        verify(userRepository, times(1)).deleteById(any(Long.class));
    }

    private UserDto createNewUserDto() {
        return UserDto.builder()
                .name("User")
                .email("user@test.com")
                .build();
    }

    private UserDto createUserDtoForUpdate() {
        return UserDto.builder()
                .id(1L)
                .name("New Name")
                .email("user@test.com")
                .build();
    }
}