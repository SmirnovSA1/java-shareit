package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByBookerId() {
        User user = User.builder()
                .name("User")
                .email("userEmail@test.com")
                .build();
        userRepository.save(user);

        Item item = Item.builder()
                .name("Item")
                .description("Описание")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(4))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking2);

        Pageable pageable = PageRequest.of(0, 10);

        List<Booking> result = bookingRepository.findAllByBookerId(1L, pageable);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfter() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);

        User user = User.builder()
                .name("User")
                .email("userEmail@test.com")
                .build();
        userRepository.save(user);

        Item item = Item.builder()
                .name("Item")
                .description("Описание")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(4))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking2);

        Pageable pageable = PageRequest.of(0, 10);

        List<Booking> result = bookingRepository
                .findAllByBookerIdAndStartBeforeAndEndAfter(1L, start, end, pageable);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findAllByBookerIdAndEndBefore() {
        LocalDateTime start = LocalDateTime.now();

        User user = User.builder()
                .name("User")
                .email("userEmail@test.com")
                .build();
        userRepository.save(user);

        Item item = Item.builder()
                .name("Item")
                .description("Описание")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(4))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking2);

        Pageable pageable = PageRequest.of(0, 10);

        List<Booking> result = bookingRepository.findAllByBookerIdAndEndBefore(1L, start, pageable);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findAllByBookerIdAndStartAfter() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);

        User user = User.builder()
                .name("User")
                .email("userEmail@test.com")
                .build();
        userRepository.save(user);

        Item item = Item.builder()
                .name("Item")
                .description("Описание")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(4))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking2);

        Pageable pageable = PageRequest.of(0, 10);

        List<Booking> result = bookingRepository.findAllByBookerIdAndStartAfter(1L, start, pageable);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findAllByBookerIdAndStatus() {
        User user = User.builder()
                .name("User")
                .email("userEmail@test.com")
                .build();
        userRepository.save(user);

        Item item = Item.builder()
                .name("Item")
                .description("Описание")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(4))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking2);

        Pageable pageable = PageRequest.of(0, 10);

        List<Booking> result = bookingRepository.findAllByBookerIdAndStatus(1L, BookingStatus.WAITING, pageable);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findAllBookingsByItem_OwnerId() {
        User user = User.builder()
                .name("User")
                .email("userEmail@test.com")
                .build();
        userRepository.save(user);

        Item item = Item.builder()
                .name("Item")
                .description("Описание")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(4))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking2);

        Pageable pageable = PageRequest.of(0, 10);

        List<Booking> result = bookingRepository.findAllBookingsByItem_OwnerId(1L, pageable);
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}