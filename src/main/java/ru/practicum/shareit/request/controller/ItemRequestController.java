package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDtoResponse addItemRequest(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Создание запроса вещи пользователем: {} \n {}", userId, itemRequestDto);
        return itemRequestService.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDtoResponse> getItemRequestListForUser(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Получение списка запросов на вещи пользователя: {}", userId);
        return itemRequestService.getItemRequestListForUser(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDtoResponse> getAllItemRequests(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получение списка всех запросов на вещи");
        return itemRequestService.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDtoResponse getItemRequestById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                     @PathVariable(name = "requestId") Long requestId) {
        log.info("Получение запроса на вещь по id: {}", requestId);
        return itemRequestService.getItemRequestById(userId, requestId);
    }
}
