package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item createItem(Long userId, ItemDto newItemDto);

    Item updateItem(Long userId, Long itemId, ItemDto updatedItemDto);

    Item getItemById(Long userId, Long itemId);

    List<Item> getItemsByOwner(Long userId);

    List<Item> getItemByText(Long userId, String text);
}
