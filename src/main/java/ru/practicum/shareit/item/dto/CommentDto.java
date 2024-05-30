package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private Item item;
    private User author;
}
