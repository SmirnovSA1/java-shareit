package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestDto {
    private Long id;
    @NotBlank
    private String description;
    private UserDto requester;
    private LocalDateTime created;
}
