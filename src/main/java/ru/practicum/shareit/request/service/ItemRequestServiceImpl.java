package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Transactional
    @Override
    public ItemRequestDtoResponse addItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        User foundUser = userMapper.toUser(userService.getUserById(userId));

        itemRequest.setRequester(foundUser);
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);

        return itemRequestMapper.toItemRequestDtoResponse(savedItemRequest);
    }

    @Override
    public List<ItemRequestDtoResponse> getItemRequestListForUser(Long userId) {
        User foundUser = userMapper.toUser(userService.getUserById(userId));
        List<ItemRequest> foundItemRequestsForUser = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(foundUser.getId());
        List<Item> foundItemsByRequestIds = itemService.getItemsByRequestIdList(foundItemRequestsForUser.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList()));

        List<ItemDto> itemDtoList = itemMapper.toListItemDto(foundItemsByRequestIds);

        List<ItemRequestDtoResponse> result = foundItemRequestsForUser.stream()
                .map(itemRequest -> {
                    ItemRequestDtoResponse tempDto = itemRequestMapper.toItemRequestDtoResponse(itemRequest);
                    tempDto.setItems(itemDtoList);
                    return tempDto;
                })
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public List<ItemRequestDtoResponse> getAllItemRequests(Long userId, Integer from, Integer size) {
        User foundUser = userMapper.toUser(userService.getUserById(userId));

        if (from < 0 || size <= 0) {
            throw new BadRequestException("Неверно указаны параметры запроса. \n " +
                    "'from' должен быть больше или равен 0, 'size' должен быть больше 0");
        }

        int pageNumber = from / size;

        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("created").descending());

        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequesterIdNot(foundUser.getId(), pageable);

        List<Item> itemsByRequestIds = itemService.getItemsByRequestIdList(itemRequestList.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList()));

        List<ItemDto> itemDtoList = itemMapper.toListItemDto(itemsByRequestIds);

        List<ItemRequestDtoResponse> result = itemRequestList.stream()
                .map(itemRequest -> {
                    ItemRequestDtoResponse tempDto = itemRequestMapper.toItemRequestDtoResponse(itemRequest);
                    tempDto.setItems(itemDtoList);
                    return tempDto;
                })
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public ItemRequestDtoResponse getItemRequestById(Long userId, Long requestId) {
        userService.getUserById(userId);

        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос на вещь по id: %d не найден", requestId)));

        List<Item> foundItems = itemService.getItemByRequestId(itemRequest.getId());
        List<ItemDto> itemsDto = itemMapper.toListItemDto(foundItems);

        ItemRequestDtoResponse result = itemRequestMapper.toItemRequestDtoResponse(itemRequest);
        result.setItems(itemsDto);

        return result;
    }
}
