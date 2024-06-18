package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank(message = "Поле 'name' обязательно к заполнению!")
    private String name;
    @NotBlank(message = "Поле 'email' обязательно к заполнению!")
    @Email(message = "Email не соответствует формату!")
    private String email;
}