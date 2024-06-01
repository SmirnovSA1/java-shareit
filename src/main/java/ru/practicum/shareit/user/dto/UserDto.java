package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotNull
    @NotBlank(message = "Поле 'name' обязательно к заполнению!")
    private String name;
    @Email(message = "Email не соответствует формату!")
    @NotBlank(message = "Поле 'email' обязательно к заполнению!")
    private String email;
}
