package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long userId, ItemDto newItemDto);

    ItemDtoInfo updateItem(Long userId, Long itemId, ItemUpdatedDto updatedItemDto);

    ItemDtoInfo getItemById(Long userId, Long itemId);

    List<ItemDtoInfo> getItemsByOwner(Long userId);

    List<ItemDtoInfo> getItemByText(Long userId, String text);

    CommentDtoResponse addComment(Long userId, Long itemId, CommentDto commentDto);
}
