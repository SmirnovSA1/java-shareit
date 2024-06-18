package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(target = "requester", ignore = true)
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    @Mapping(target = "items", ignore = true)
    ItemRequestDtoResponse toItemRequestDtoResponse(ItemRequest itemRequest);

    @Mapping(target = "requester", ignore = true)
    ItemRequest toItemRequestFromDtoResponse(ItemRequestDtoResponse itemRequestDtoResponse);
}
