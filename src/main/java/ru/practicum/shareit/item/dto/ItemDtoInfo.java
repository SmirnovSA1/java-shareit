package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.util.List;

@Data
@Builder
public class ItemDtoInfo {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingItemDto nextBooking;
    private BookingItemDto lastBooking;
    private List<CommentDtoResponse> comments;
}
