package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody ItemDto newItemDto) {
        log.info("Создание вещи: {}", newItemDto);
        return ResponseEntity.ok().body(itemService.createItem(userId, newItemDto)).getBody();
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoInfo updateItem(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                              @PathVariable(name = "itemId") Long itemId,
                              @Valid @RequestBody ItemUpdatedDto updatedItemDto) {
        log.info("Обновление данных вещи: {}", updatedItemDto);
        return ResponseEntity.ok().body(itemService.updateItem(userId, itemId, updatedItemDto)).getBody();
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoInfo getItemById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                   @PathVariable(name = "itemId") Long itemId) {
        log.info("Получение вещи по id: {}", itemId);
        return ResponseEntity.ok().body(itemService.getItemById(userId, itemId)).getBody();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoInfo> getItemsByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.info("Полвучение вещей пользователя с id: {}", userId);
        return ResponseEntity.ok().body(itemService.getItemsByOwner(userId, from, size)).getBody();
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoInfo> getItemByText(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                           @RequestParam(name = "text") String text,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение пользователем {} вещи по наименованию: {}", userId, text);
        return ResponseEntity.ok().body(itemService.getItemByText(userId, text, from, size)).getBody();
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse addComment(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                         @PathVariable Long itemId,
                                         @Valid @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
