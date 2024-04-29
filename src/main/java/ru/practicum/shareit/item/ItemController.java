package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody ItemDto newItemDto) {
        log.info("Создание вещи: {}", newItemDto);
        Item createdItem = itemService.createItem(userId, newItemDto);
        return ItemMapper.toItemDto(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                              @PathVariable(name = "itemId") Long itemId,
                              @RequestBody ItemDto updatedItemDto) {
        log.info("Обновление данных вещи: {}", updatedItemDto);
        Item updatedItem = itemService.updateItem(userId, itemId, updatedItemDto);
        return ItemMapper.toItemDto(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                               @PathVariable(name = "itemId") Long itemId) {
        log.info("Получение вещи по id: {}", itemId);
        Item foundItem = itemService.getItemById(userId, itemId);
        return ItemMapper.toItemDto(foundItem);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Полвучение вещей пользователя с id: {}", userId);
        List<Item> itemListByOwner = itemService.getItemsByOwner(userId);
        return itemListByOwner.stream()
                .map(item -> ItemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByText(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                  @RequestParam(name = "text") String text) {
        log.info("Получение пользователем {} вещи по наименованию: {}", userId, text);
        List<Item> foundItems = itemService.getItemByText(userId, text);
        return foundItems.stream()
                .map(item -> ItemMapper.toItemDto(item))
                .collect(Collectors.toList());
    }
}
