package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private ItemDto item;
    private UserDto author;
}
