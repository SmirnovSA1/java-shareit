package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto newItemDto) {
        return itemService.createItem(userId, newItemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoInfo updateItem(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                              @PathVariable(name = "itemId") Long itemId,
                              @RequestBody ItemUpdatedDto updatedItemDto) {
        return itemService.updateItem(userId, itemId, updatedItemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoInfo getItemById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                   @PathVariable(name = "itemId") Long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoInfo> getItemsByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getItemsByOwner(userId, from, size);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoInfo> getItemByText(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                           @RequestParam(name = "text") String text,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getItemByText(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse addComment(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                         @PathVariable(name = "itemId") Long itemId,
                                         @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
