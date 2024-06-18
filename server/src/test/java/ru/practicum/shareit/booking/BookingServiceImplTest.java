package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
class BookingServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private BookingServiceImpl bookingService;
    private LocalDateTime start;
    private LocalDateTime end;
    private UserDto userDto;
    private ItemDto itemDto;
    private BookingDto bookingDto;
    private BookingDtoExtended bookingDtoExtended;
    private User user;
    private User user2;
    private Item item;
    private Booking booking;


    @BeforeEach
    void setUp() {
        start = LocalDateTime.now();
        end = LocalDateTime.now().plusHours(1);
        userDto = UserDto.builder()
                .id(1L)
                .name("User")
                .email("userEmail@test.ru")
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("описание")
                .available(true)
                .build();
        bookingDto = BookingDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .itemId(1L)
                .booker(1L)
                .status(BookingStatus.WAITING)
                .build();
        bookingDtoExtended = BookingDtoExtended.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(itemDto)
                .booker(userDto)
                .status(BookingStatus.WAITING)
                .build();
        user = User.builder()
                .id(1L)
                .name("User")
                .email("userEmail@test.ru")
                .build();
        user2 = User.builder()
                .id(2L)
                .name("User 2")
                .email("user2Email@test.ru")
                .build();
        item = Item.builder()
                .id(1L)
                .name("item")
                .description("описание")
                .available(true)
                .owner(user2)
                .build();
        booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void createBooking() {
        when(bookingMapper.toBooking(any(BookingDto.class))).thenReturn(booking);
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        BookingDtoExtended result = bookingService.createBooking(1L, bookingDto);

        assertEquals(bookingDto.getStart(), result.getStart());
        assertEquals(bookingDto.getEnd(), result.getEnd());
        assertEquals(BookingStatus.WAITING, result.getStatus());
        assertEquals(itemDto, result.getItem());
        assertEquals(userDto, result.getBooker());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void confirmOrRejectBooking() {
        item.setOwner(user);
        Booking bookingApproved = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        BookingDtoExtended bookingDtoExtendedApproved = BookingDtoExtended.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(itemDto)
                .booker(userDto)
                .status(BookingStatus.APPROVED)
                .build();

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingApproved);
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtendedApproved);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        BookingDtoExtended result = bookingService.confirmOrRejectBooking(1L, 1L,true);

        assertEquals(BookingStatus.APPROVED, result.getStatus());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
        assertEquals(itemDto, result.getItem());
        assertEquals(userDto, result.getBooker());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void getBookingById() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        BookingDtoExtended result = bookingService.getBookingById(1L, 1L);

        assertEquals(bookingDtoExtended.getId(), result.getId());
        assertEquals(bookingDtoExtended.getBooker(), result.getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.getStatus());
    }

    @Test
    void getUserBookingsAll() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(bookingRepository.findAllByBookerId(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getUserBookings(1L, "ALL", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getUserBookingsCurrent() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                anyLong(), any(LocalDateTime.class), any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getUserBookings(1L, "CURRENT", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getUserBookingsPast() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(bookingRepository.findAllByBookerIdAndEndBefore(
                anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getUserBookings(1L, "PAST", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getUserBookingsFuture() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(bookingRepository.findAllByBookerIdAndStartAfter(
                anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getUserBookings(1L, "FUTURE", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getUserBookingsWaiting() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(bookingRepository.findAllByBookerIdAndStatus(
                anyLong(), any(BookingStatus.class), any(Pageable.class))).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getUserBookings(1L, "WAITING", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getUserBookingsRejected() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(bookingRepository.findAllByBookerIdAndStatus(
                anyLong(), any(BookingStatus.class), any(Pageable.class))).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getUserBookings(1L, "REJECTED", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByItemOwnerBookingsAll() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(bookingRepository.findAllBookingsByItem_OwnerId(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getByItemOwnerBookings(1L, "ALL", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByItemOwnerBookingsCurrent() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(bookingRepository.findAllBookingsByItem_OwnerIdAndStartBeforeAndEndAfter(
                anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getByItemOwnerBookings(1L, "CURRENT", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByItemOwnerBookingsPast() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(bookingRepository.findAllBookingsByItem_OwnerIdAndEndBefore(
                anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getByItemOwnerBookings(1L, "PAST", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByItemOwnerBookingsFuture() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(bookingRepository.findAllBookingsByItem_OwnerIdAndStartAfter(
                anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getByItemOwnerBookings(1L, "FUTURE", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByItemOwnerBookingsWaiting() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(bookingRepository.findAllBookingsByItem_OwnerIdAndStatus(
                anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getByItemOwnerBookings(1L, "WAITING", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getByItemOwnerBookingsRejected() {
        when(userService.getUserById(any(Long.class))).thenReturn(userDto);
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(bookingRepository.findAllBookingsByItem_OwnerIdAndStatus(
                anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoExtended(any(Booking.class))).thenReturn(bookingDtoExtended);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<BookingDtoExtended> result = bookingService.getByItemOwnerBookings(1L, "REJECTED", 0, 10);

        assertEquals(bookingDtoExtended.getId(), result.get(0).getId());
        assertEquals(bookingDtoExtended.getBooker(), result.get(0).getBooker());
        assertEquals(bookingDtoExtended.getStart(), result.get(0).getStart());
        assertEquals(bookingDtoExtended.getEnd(), result.get(0).getEnd());
        assertEquals(bookingDtoExtended.getItem(), result.get(0).getItem());
        assertEquals(bookingDtoExtended.getStatus(), result.get(0).getStatus());
    }
}