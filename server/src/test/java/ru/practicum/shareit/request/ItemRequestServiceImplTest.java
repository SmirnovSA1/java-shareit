package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRequestMapper itemRequestMapper;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ItemService itemService;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    private UserDto requesterDto;
    private User requester;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDtoResponse itemRequestDtoResponse;
    private LocalDateTime created;
    private Item item;
    private ItemDto itemDto;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        created = LocalDateTime.now();
        requesterDto = UserDto.builder()
                .id(1L)
                .name("User")
                .email("user@email.ru")
                .build();
        requester = User.builder()
                .id(1L)
                .name("User")
                .email("user@email.ru")
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .description("Описание")
                .build();
        itemRequestDtoResponse = ItemRequestDtoResponse.builder()
                .id(1L)
                .description("Описание")
                .created(created)
                .build();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Описание")
                .created(created)
                .requester(requester)
                .build();
        item = Item.builder()
                .id(1L)
                .name("item")
                .description("описание вещи")
                .available(true)
                .owner(requester)
                .request(itemRequest)
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("описание вещи")
                .available(true)
                .requestId(1L)
                .build();
    }

    @Test
    void addItemRequest() {
        when(itemRequestMapper.toItemRequest(any(ItemRequestDto.class))).thenReturn(
                new ItemRequest(null, "Описание", null, null)
        );
        when(userService.getUserById(any(Long.class))).thenReturn(requesterDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(requester);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        when(itemRequestMapper.toItemRequestDtoResponse(any(ItemRequest.class))).thenReturn(itemRequestDtoResponse);

        ItemRequestDtoResponse result = itemRequestService.addItemRequest(1L, itemRequestDto);

        assertEquals(itemRequestDtoResponse, result);
        verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    void getItemRequestListForUser() {
        List<ItemRequest> itemRequests = List.of(itemRequest);
        List<Item> items = List.of(item);
        List<ItemDto> itemsDto = List.of(itemDto);
        itemRequestDtoResponse.setItems(itemsDto);

        when(userService.getUserById(any(Long.class))).thenReturn(requesterDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(requester);
        when(itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(any(Long.class))).thenReturn(itemRequests);
        when(itemService.getItemsByRequestIdList(any(List.class))).thenReturn(items);
        when(itemMapper.toListItemDto(anyList())).thenReturn(itemsDto);
        when(itemRequestMapper.toItemRequestDtoResponse(any(ItemRequest.class))).thenReturn(itemRequestDtoResponse);

        List<ItemRequestDtoResponse> result = itemRequestService.getItemRequestListForUser(1L);

        assertEquals(List.of(itemRequestDtoResponse), result);
    }

    @Test
    void getAllItemRequests() {
        int from = 0;
        int size = 10;
        List<ItemRequest> itemRequests = List.of(itemRequest);
        List<Item> items = List.of(item);
        List<ItemDto> itemsDto = List.of(itemDto);
        itemRequestDtoResponse.setItems(itemsDto);

        when(userService.getUserById(any(Long.class))).thenReturn(requesterDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(requester);
        when(itemRequestRepository.findAllByRequesterIdNot(any(Long.class), any(Pageable.class))).thenReturn(itemRequests);
        when(itemService.getItemsByRequestIdList(any(List.class))).thenReturn(items);
        when(itemMapper.toListItemDto(anyList())).thenReturn(itemsDto);
        when(itemRequestMapper.toItemRequestDtoResponse(any(ItemRequest.class))).thenReturn(itemRequestDtoResponse);

        List<ItemRequestDtoResponse> result = itemRequestService.getAllItemRequests(1L, from, size);

        assertEquals(List.of(itemRequestDtoResponse), result);
    }

    @Test
    void getItemRequestById() {
        List<Item> items = List.of(item);
        List<ItemDto> itemsDto = List.of(itemDto);

        when(itemRequestRepository.findById(any(Long.class))).thenReturn(Optional.of(itemRequest));
        when(itemService.getItemByRequestId(itemRequest.getId())).thenReturn(items);
        when(itemMapper.toListItemDto(anyList())).thenReturn(itemsDto);
        when(itemRequestMapper.toItemRequestDtoResponse(any(ItemRequest.class))).thenReturn(itemRequestDtoResponse);

        ItemRequestDtoResponse result = itemRequestService.getItemRequestById(1L, 1L);

        assertEquals(itemRequest.getCreated(), result.getCreated());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemsDto, result.getItems());
    }
}