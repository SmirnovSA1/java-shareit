package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class BookingServiceIntegrationTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void getAllBookings() {
        User user = User.builder()
                .name("User")
                .email("useremail@test.ru")
                .build();
        userRepository.save(user);

        Item item1 = Item.builder()
                .name("Item 1")
                .description("Описание 1")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
                .name("Item 2")
                .description("Описание 2")
                .available(false)
                .owner(user)
                .build();
        itemRepository.save(item2);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .item(item1)
                .status(BookingStatus.APPROVED)
                .booker(user)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now().plusDays(4))
                .end(LocalDateTime.now().plusDays(6))
                .item(item2)
                .status(BookingStatus.APPROVED)
                .booker(user)
                .build();
        bookingRepository.save(booking2);

        List<BookingDtoExtended> bookings = bookingService.getUserBookings(user.getId(), "CURRENT", 0, 2);

        assertEquals(1, bookings.size());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
    }
}
