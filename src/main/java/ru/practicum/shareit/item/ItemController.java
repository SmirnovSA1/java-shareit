package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

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
        return ResponseEntity.ok().body(itemService.createItem(userId, newItemDto)).getBody();
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                              @PathVariable(name = "itemId") Long itemId,
                              @RequestBody ItemDto updatedItemDto) {
        log.info("Обновление данных вещи: {}", updatedItemDto);
        return ResponseEntity.ok().body(itemService.updateItem(userId, itemId, updatedItemDto)).getBody();
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                               @PathVariable(name = "itemId") Long itemId) {
        log.info("Получение вещи по id: {}", itemId);
        return ResponseEntity.ok().body(itemService.getItemById(userId, itemId)).getBody();
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Полвучение вещей пользователя с id: {}", userId);
        return ResponseEntity.ok().body(itemService.getItemsByOwner(userId)).getBody();
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByText(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                  @RequestParam(name = "text") String text) {
        log.info("Получение пользователем {} вещи по наименованию: {}", userId, text);
        return ResponseEntity.ok().body(itemService.getItemByText(userId, text)).getBody();
    }
}
