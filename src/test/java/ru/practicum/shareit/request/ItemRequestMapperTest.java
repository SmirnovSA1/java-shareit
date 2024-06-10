package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ItemRequestMapperTest {
    @Autowired
    private ItemRequestMapper itemRequestMapper;

    @Test
    void toItemRequest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("Описание")
                .build();

        ItemRequest itemRequest = ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .build();

        ItemRequest result = itemRequestMapper.toItemRequest(itemRequestDto);

        assertEquals(itemRequest, result);
    }

    @Test
    void toItemRequestDtoResponse() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Описание")
                .build();

        ItemRequestDtoResponse itemRequestDtoResponse = ItemRequestDtoResponse.builder()
                .id(1L)
                .description("Описание")
                .build();

        ItemRequestDtoResponse result = itemRequestMapper.toItemRequestDtoResponse(itemRequest);

        assertEquals(itemRequestDtoResponse, result);
    }

    @Test
    void toItemRequestFromDtoResponse() {
        ItemRequestDtoResponse itemRequestDtoResponse = ItemRequestDtoResponse.builder()
                .id(1L)
                .description("Описание")
                .build();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Описание")
                .build();

        ItemRequest result = itemRequestMapper.toItemRequestFromDtoResponse(itemRequestDtoResponse);

        assertEquals(itemRequest, result);
    }
}
