package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDtoResponse addItemRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDtoResponse> getItemRequestListForUser(Long userId);

    List<ItemRequestDtoResponse> getAllItemRequests(Long userId, Integer from, Integer size);

    ItemRequestDtoResponse getItemRequestById(Long userId, Long requestId);
}
