package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    @NotEmpty
    private String name;
    @Email
    @NotEmpty
    private String email;
}
