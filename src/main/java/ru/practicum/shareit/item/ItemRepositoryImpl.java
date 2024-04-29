package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> itemStorage = new HashMap<>();
    private long generatedId = 0L;

    @Override
    public Item createItem(Long userId, ItemDto newItemDto) {
        Item createdItem = ItemMapper.toItem(userId, newItemDto);
        createdItem.setId(generateId());
        itemStorage.put(createdItem.getId(), createdItem);
        return createdItem;
    }

    @Override
    public Item updateItem(Long userId, Long itemId, ItemDto updatedItemDto) {
        Item updatedItem = itemStorage.get(itemId);
        updatedItem = Item.builder()
                .id(updatedItem.getId())
                .name(updatedItemDto.getName() != null ? updatedItemDto.getName() : updatedItem.getName())
                .description(updatedItemDto.getDescription() != null ? updatedItemDto.getDescription() : updatedItem.getDescription())
                .available(updatedItemDto.getAvailable() != null ? updatedItemDto.getAvailable() : updatedItem.getAvailable())
                .owner(userId)
                .request(updatedItem.getRequest())
                .build();

        itemStorage.put(updatedItem.getId(), updatedItem);
        return updatedItem;
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemStorage.get(itemId);
    }

    @Override
    public List<Item> getItemsByOwner(Long userId) {
        return itemStorage.entrySet().stream()
                .filter(id -> id.getValue().getOwner().equals(userId))
                .map(id -> id.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemByText(String text) {
        return itemStorage.entrySet().stream()
                .filter(item -> item.getValue().getAvailable() == true)
                .filter(item -> item.getValue().getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getValue().getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(item -> item.getValue())
                .collect(Collectors.toList());
    }

    private Long generateId() {
        return ++generatedId;
    }
}
