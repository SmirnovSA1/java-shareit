package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotAllowedException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto createItem(Long userId, ItemDto newItemDto) {
        userService.getUserById(userId);
        Item createdItem = itemMapper.toItem(userId, newItemDto);
        createdItem = itemRepository.createItem(userId, createdItem);
        return itemMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto updatedItemDto) {
        userService.getUserById(userId);
        changeIsAllowed(userId, itemId);
        Item updatedItem = itemRepository.updateItem(userId, itemId, updatedItemDto);
        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        userService.getUserById(userId);
        Item foundItem = itemRepository.getItemById(itemId);

        if (foundItem == null) throw new NotFoundException(String.format("Не найдена вещь c id %d", itemId));

        return itemMapper.toItemDto(foundItem);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long userId) {
        userService.getUserById(userId);
        List<Item> itemsByOwner = itemRepository.getItemsByOwner(userId);
        return itemsByOwner.stream()
                .map(item -> itemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemByText(Long userId, String text) {
        userService.getUserById(userId);

        if (text.trim().isBlank()) return new ArrayList<>();

        List<Item> foundItems = itemRepository.getItemByText(text);
        return foundItems.stream()
                .map(item -> itemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

    private void changeIsAllowed(Long userId, Long itemId) {
        Long foundOwner = getItemById(userId, itemId).getOwner();
        if (!foundOwner.equals(userId)) throw new NotAllowedException("Редактировать вещь может только её владелец.");
    }
}
