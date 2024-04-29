package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotAllowedException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Item createItem(Long userId, ItemDto newItemDto) {
        userService.getUserById(userId);
        return itemRepository.createItem(userId, newItemDto);
    }

    @Override
    public Item updateItem(Long userId, Long itemId, ItemDto updatedItemDto) {
        userService.getUserById(userId);
        changeIsAllowed(userId, itemId);
        return itemRepository.updateItem(userId, itemId, updatedItemDto);
    }

    @Override
    public Item getItemById(Long userId, Long itemId) {
        userService.getUserById(userId);
        Item foundItem = itemRepository.getItemById(itemId);

        if (foundItem == null) throw new NotFoundException(String.format("Не найдена вещь c id %d", itemId));

        return foundItem;
    }

    @Override
    public List<Item> getItemsByOwner(Long userId) {
        userService.getUserById(userId);
        return itemRepository.getItemsByOwner(userId);
    }

    @Override
    public List<Item> getItemByText(Long userId, String text) {
        userService.getUserById(userId);

        if (text.trim().isBlank()) return new ArrayList<>();

        return itemRepository.getItemByText(text);
    }

    private void changeIsAllowed(Long userId, Long itemId) {
        Long foundOwner = getItemById(userId, itemId).getOwner();
        if (!foundOwner.equals(userId)) throw new NotAllowedException("Редактировать вещь может только её владелец.");
    }
}
