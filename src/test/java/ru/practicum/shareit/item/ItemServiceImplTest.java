package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.service.ItemRequestService;
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
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;;
    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemServiceImpl itemService;
    private UserDto userDto;
    private User user;
    private ItemDto itemDto;
    private Item item;
    private ItemUpdatedDto itemUpdatedDto;
    private Item updatedItem;
    private ItemDtoInfo itemDtoInfo;
    private ItemDtoInfo itemDtoInfoUpdated;
    private CommentDto commentDto;
    private Comment comment;
    private CommentDtoResponse commentDtoResponse;
    private ItemRequestDtoResponse itemRequestDtoResponse;
    private Booking nextBooking;
    private Booking lastBooking;
    private BookingItemDto nextBookingItemDto;
    private BookingItemDto lastBookingItemDto;
    private LocalDateTime created;
    private LocalDateTime start1;
    private LocalDateTime end1;
    private LocalDateTime start2;
    private LocalDateTime end2;

    @BeforeEach
    void setUp() {
        created = LocalDateTime.now();
        start1 = LocalDateTime.now().minusHours(2);
        end1 = LocalDateTime.now().minusHours(1);
        start2 = LocalDateTime.now().plusHours(1);
        end2 = LocalDateTime.now().plusHours(2);

        item = Item.builder()
                .id(1L)
                .name("item")
                .description("описание")
                .available(true)
                .requestId(1L)
                .build();
    }

    @Test
    void createItem() {
        userDto = createUserDto();
        user = createUser();
        item.setOwner(user);
        itemDto = createItemDto();
        itemRequestDtoResponse = createItemRequestDtoResponse();

        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(itemRequestService.getItemRequestById(user.getId(), itemDto.getId())).thenReturn(itemRequestDtoResponse);
        when(itemMapper.toItem(any(ItemDto.class))).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);

        ItemDto result = itemService.createItem(1L, itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
        assertEquals(itemDto.getRequestId(), result.getRequestId());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void getItemById() {
        userDto = createUserDto();
        user = createUser();
        item.setOwner(user);
        itemDtoInfo = createItemDtoInfo();
        lastBooking = createLastBooking();
        lastBooking.setBooker(user);
        lastBookingItemDto = createLastBookingItemDto();
        nextBooking = createNextBooking();
        nextBooking.setBooker(user);
        nextBookingItemDto = createNextBookingItemDto();
        itemDtoInfo.setLastBooking(lastBookingItemDto);
        itemDtoInfo.setNextBooking(nextBookingItemDto);
        List<CommentDtoResponse> commentDtoResponseList = List.of();
        itemDtoInfo.setComments(commentDtoResponseList);

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findAllByItemIdOrderByEndDesc(any(Long.class))).thenReturn(List.of(lastBooking));
        when(commentRepository.findAllByItemId(any(Long.class))).thenReturn(List.of());
        when(itemMapper.toItemDtoInfo(any(Item.class))).thenReturn(itemDtoInfo);
        when(commentMapper.toListCommentDtoResponse(any(List.class))).thenReturn(commentDtoResponseList);

        ItemDtoInfo result = itemService.getItemById(1L, 1L);

        assertNotNull(result);
        assertEquals(itemDtoInfo.getId(), result.getId());
        assertEquals(itemDtoInfo.getName(), result.getName());
        assertEquals(itemDtoInfo.getDescription(), result.getDescription());
        assertEquals(itemDtoInfo.getAvailable(), result.getAvailable());
        assertEquals(itemDtoInfo.getLastBooking(), result.getLastBooking());
        assertEquals(itemDtoInfo.getNextBooking(), result.getNextBooking());
        assertEquals(itemDtoInfo.getComments(), result.getComments());
    }

    @Test
    void getItemsByOwner() {
        userDto = createUserDto();
        user = createUser();
        item.setOwner(user);
        List<Item> items = List.of(item, item);
        itemDtoInfo = createItemDtoInfo();
        lastBooking = createLastBooking();
        lastBooking.setBooker(user);
        lastBookingItemDto = createLastBookingItemDto();
        nextBooking = createNextBooking();
        nextBooking.setBooker(user);
        nextBookingItemDto = createNextBookingItemDto();
        itemDtoInfo.setLastBooking(lastBookingItemDto);
        itemDtoInfo.setNextBooking(nextBookingItemDto);
        ItemDtoInfo itemDtoInfo2 = createItemDtoInfo();
        itemDtoInfo2.setLastBooking(lastBookingItemDto);
        itemDtoInfo2.setNextBooking(nextBookingItemDto);
        List<Booking> bookings = List.of(lastBooking, nextBooking);
        List<CommentDtoResponse> commentDtoResponseList = List.of();

        when(itemRepository.findByOwnerId(any(Long.class), any(Pageable.class))).thenReturn(items);
        when(bookingRepository.findAllByItemInOrderByEndDesc(items)).thenReturn(bookings);
        when(commentRepository.findAllByItemIn(any(List.class))).thenReturn(List.of());
        when(itemMapper.toItemDtoInfo(any(Item.class))).thenReturn(itemDtoInfo);
        when(commentMapper.toListCommentDtoResponse(any(List.class))).thenReturn(commentDtoResponseList);

        List<ItemDtoInfo> result = itemService.getItemsByOwner(1L, 0, 10);

        assertNotNull(result);
        assertEquals(itemDtoInfo.getId(), result.get(0).getId());
        assertEquals(itemDtoInfo.getName(), result.get(0).getName());
        assertEquals(itemDtoInfo.getDescription(), result.get(0).getDescription());
        assertEquals(itemDtoInfo.getAvailable(), result.get(0).getAvailable());
        assertEquals(itemDtoInfo.getLastBooking(), result.get(0).getLastBooking());
        assertEquals(itemDtoInfo.getNextBooking(), result.get(0).getNextBooking());
        assertEquals(itemDtoInfo.getComments(), result.get(0).getComments());
        assertEquals(itemDtoInfo2.getId(), result.get(1).getId());
        assertEquals(itemDtoInfo2.getName(), result.get(1).getName());
        assertEquals(itemDtoInfo2.getDescription(), result.get(1).getDescription());
        assertEquals(itemDtoInfo2.getAvailable(), result.get(1).getAvailable());
        assertEquals(itemDtoInfo2.getLastBooking(), result.get(1).getLastBooking());
        assertEquals(itemDtoInfo2.getNextBooking(), result.get(1).getNextBooking());
    }

    @Test
    void getItemByText() {
        userDto = createUserDto();
        user = createUser();
        item.setOwner(user);
        itemDto = createItemDto();
        itemDtoInfo = createItemDtoInfo();

        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(itemRepository.findByNameContainingIgnoreCase(any(String.class), any(Pageable.class)))
                .thenReturn(List.of(item));
        when(itemMapper.toListItemDtoInfo(any(List.class))).thenReturn(List.of(itemDtoInfo));

        List<ItemDtoInfo> result = itemService.getItemByText(1L,"оПис", 0, 10);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.get(0).getName());
        assertEquals(itemDto.getDescription(), result.get(0).getDescription());
        assertEquals(itemDto.getAvailable(), result.get(0).getAvailable());
    }

    @Test
    void updateItem() {
        userDto = createUserDto();
        user = createUser();
        item.setOwner(user);
        updatedItem = createItemUpdated();
        itemDtoInfoUpdated = createItemDtoInfoUpdated();
        itemUpdatedDto = createItemUpdatedDto();

        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(itemMapper.toItemFromUpdatedDto(any(ItemUpdatedDto.class))).thenReturn(updatedItem);
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);
        when(itemMapper.toItemDtoInfo(any(Item.class))).thenReturn(itemDtoInfoUpdated);

        ItemDtoInfo result = itemService.updateItem(1L, 1L, itemUpdatedDto);

        assertNotNull(result);
        assertEquals(itemUpdatedDto.getId(), result.getId());
        assertEquals(itemUpdatedDto.getName(), result.getName());
        assertEquals(itemUpdatedDto.getDescription(), result.getDescription());
        assertEquals(itemUpdatedDto.getAvailable(), result.getAvailable());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void addComment() {
        userDto = createUserDto();
        user = createUser();
        user.setId(1L);
        item.setOwner(user);
        itemDto = createItemDto();
        lastBooking = createLastBooking();
        lastBooking.setBooker(user);
        commentDto = createCommentDto(itemDto, userDto);
        comment = createComment(item, user);
        commentDtoResponse = createCommentDtoResponse();

        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findAllByItemIdOrderByEndDesc(any(Long.class))).thenReturn(List.of(lastBooking));
        when(commentMapper.toComment(any(CommentDto.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toCommentDtoResponse(any(Comment.class))).thenReturn(commentDtoResponse);

        CommentDtoResponse result = itemService.addComment(1L, 1L, commentDto);

        assertEquals(commentDto.getText(), result.getText());
        assertEquals(commentDto.getAuthor().getName(), result.getAuthorName());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void getItemsByRequestIdList() {
        user = createUser();
        item.setOwner(user);
        Item item2 = Item.builder()
                .id(2L)
                .name("Item 2")
                .description("Описание 2")
                .available(true)
                .owner(user)
                .requestId(2L)
                .build();
        List<Item> items = List.of(item, item2);

        when(itemRepository.findAllByRequestIdIn(any(List.class))).thenReturn(items);

        List<Item> result = itemService.getItemsByRequestIdList(List.of(1L, 2L));

        assertNotNull(result);
        assertEquals(item.getId(), result.get(0).getId());
        assertEquals(item.getName(), result.get(0).getName());
        assertEquals(item.getDescription(), result.get(0).getDescription());
        assertEquals(item.getAvailable(), result.get(0).getAvailable());
        assertEquals(item2.getId(), result.get(1).getId());
        assertEquals(item2.getName(), result.get(1).getName());
        assertEquals(item2.getDescription(), result.get(1).getDescription());
        assertEquals(item2.getAvailable(), result.get(1).getAvailable());
    }

    @Test
    void getItemByRequestId() {
        user = createUser();
        item.setOwner(user);
        Item item2 = Item.builder()
                .id(2L)
                .name("Item 2")
                .description("Описание 2")
                .available(true)
                .owner(user)
                .requestId(2L)
                .build();
        List<Item> items = List.of(item, item2);

        when(itemRepository.findAllByRequestId(any(Long.class))).thenReturn(List.of(item));

        List<Item> result = itemService.getItemByRequestId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getId(), result.get(0).getId());
        assertEquals(item.getName(), result.get(0).getName());
        assertEquals(item.getDescription(), result.get(0).getDescription());
        assertEquals(item.getAvailable(), result.get(0).getAvailable());
    }

    private UserDto createUserDto() {
        return UserDto.builder()
                .name("User")
                .email("userEmail@test.ru")
                .build();
    }

    private User createUser() {
        return User.builder()
                .name("User")
                .email("userEmail@test.ru")
                .build();
    }

    private ItemDto createItemDto() {
        return ItemDto.builder()
                .id(1L)
                .name("item")
                .description("описание")
                .available(true)
                .requestId(1L)
                .build();
    }

    private ItemRequestDtoResponse createItemRequestDtoResponse() {
        return ItemRequestDtoResponse.builder()
                .id(1L)
                .description("Описание")
                .created(created)
                .build();
    }

    private ItemDtoInfo createItemDtoInfo() {
        return ItemDtoInfo.builder()
                .id(1L)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    private Item createItemUpdated() {
        return Item.builder()
                .id(1L)
                .name("Item Updated")
                .description("Новое описание")
                .available(false)
                .build();
    }

    private ItemUpdatedDto createItemUpdatedDto() {
        return ItemUpdatedDto.builder()
                .id(1L)
                .name("Item Updated")
                .description("Новое описание")
                .available(false)
                .build();
    }

    private ItemDtoInfo createItemDtoInfoUpdated() {
        return ItemDtoInfo.builder()
                .id(1L)
                .name("Item Updated")
                .description("Новое описание")
                .available(false)
                .build();
    }

    private Booking createLastBooking() {
        return Booking.builder()
                .id(1L)
                .start(start1)
                .end(end1)
                .item(item)
                .status(BookingStatus.APPROVED)
                .build();
    }

    private Booking createNextBooking() {
        return Booking.builder()
                .id(1L)
                .start(start2)
                .end(end2)
                .item(item)
                .status(BookingStatus.APPROVED)
                .build();
    }

    private BookingItemDto createLastBookingItemDto() {
        return BookingItemDto.builder()
                .id(1L)
                .start(start1)
                .end(end1)
                .itemId(1L)
                .bookerId(1L)
                .status(BookingStatus.APPROVED)
                .build();
    }

    private BookingItemDto createNextBookingItemDto() {
        return BookingItemDto.builder()
                .id(1L)
                .start(start2)
                .end(end2)
                .itemId(1L)
                .bookerId(1L)
                .status(BookingStatus.APPROVED)
                .build();
    }

    private CommentDto createCommentDto(ItemDto itemDto, UserDto userDto) {
        return CommentDto.builder()
                .id(1L)
                .text("Some text")
                .item(itemDto)
                .author(userDto)
                .build();
    }

    private Comment createComment(Item item, User user) {
        return Comment.builder()
                .id(1L)
                .text("Some text")
                .item(item)
                .author(user)
                .build();
    }

    private CommentDtoResponse createCommentDtoResponse() {
        return CommentDtoResponse.builder()
                .id(1L)
                .text("Some text")
                .authorName("User")
                .created(created)
                .build();
    }
}