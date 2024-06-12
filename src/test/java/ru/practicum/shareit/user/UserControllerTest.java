package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.response.Response;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = createUserResponseDto();
    }

    @SneakyThrows
    @Test
    void createUser() {
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("User"))
                .andExpect(jsonPath("$.email").value("user@test.com"));
    }

    @SneakyThrows
    @Test
    void createUser_ThrowsMethodArgumentNotValidException() {
        UserDto userDtoInvalidEmail = UserDto.builder()
                .name("User")
                .email("testEmail")
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInvalidEmail)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllUsers() {
        UserDto userDto2 = createUserResponseDto();
        userDto2.setName("User 2");
        userDto2.setEmail("user2@test.com");

        when(userService.getAllUsers())
                .thenReturn(List.of(userDto, userDto2));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User"))
                .andExpect(jsonPath("$[0].email").value("user@test.com"))
                .andExpect(jsonPath("$[1].name").value("User 2"))
                .andExpect(jsonPath("$[1].email").value("user2@test.com"));
    }

    @SneakyThrows
    @Test
    void getUserById() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value("User"))
                .andExpect(jsonPath("$.email").value("user@test.com"));
    }

    @SneakyThrows
    @Test
    void getUserByIdInvalid() {
        when(userService.getUserById(any(Long.class))).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/users/52")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void updateUser() {
        when(userService.updateUser(any(Long.class), any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(patch("/users/" + userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("User"))
                .andExpect(jsonPath("$.email").value("user@test.com"));
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        ResponseEntity<Response> response = new ResponseEntity<>(
                new Response("Успех", "Пользователь с id 1 удален"), HttpStatus.OK);

        when(userService.deleteUserById(any(Long.class))).thenReturn(response);

        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Успех"))
                .andExpect(jsonPath("$.description").value("Пользователь с id 1 удален"));
    }

    private UserDto createUserResponseDto() {
        return UserDto.builder()
                .id(1L)
                .name("User")
                .email("user@test.com")
                .build();
    }
}
