package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item createItem(Long ownerId, ItemDto newItemDto);

    Item updateItem(Long userId, Long itemId, ItemDto updatedItemDto);

    Item getItemById(Long itemId);

    List<Item> getItemsByOwner(Long ownerId);

    List<Item> getItemByText(String text);
}
