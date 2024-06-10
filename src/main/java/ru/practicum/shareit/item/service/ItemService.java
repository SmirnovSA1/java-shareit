package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long userId, ItemDto newItemDto);

    ItemDtoInfo updateItem(Long userId, Long itemId, ItemUpdatedDto updatedItemDto);

    ItemDtoInfo getItemById(Long userId, Long itemId);

    List<ItemDtoInfo> getItemsByOwner(Long userId, Integer from, Integer size);

    List<ItemDtoInfo> getItemByText(Long userId, String text, Integer from, Integer size);

    CommentDtoResponse addComment(Long userId, Long itemId, CommentDto commentDto);

    List<Item> getItemsByRequestIdList(List<Long> requestIdList);

    List<Item> getItemByRequestId(Long requestId);
}
