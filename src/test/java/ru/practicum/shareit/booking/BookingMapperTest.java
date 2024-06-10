package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BookingMapperTest {
    @Autowired
    private BookingMapper bookingMapper;

    @Test
    void toBooking() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(BookingStatus.WAITING)
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(BookingStatus.WAITING)
                .build();

        Booking result = bookingMapper.toBooking(bookingDto);

        assertEquals(booking, result);
    }

    @Test
    void toBookingDtoExtended() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(BookingStatus.WAITING)
                .build();

        BookingDtoExtended bookingDtoExtended = BookingDtoExtended.builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(BookingStatus.WAITING)
                .build();

        BookingDtoExtended result = bookingMapper.toBookingDtoExtended(booking);

        assertEquals(bookingDtoExtended, result);
    }

    @Test
    void toBookingItemDto() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        Booking booking = Booking.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(Item.builder().id(1L).build())
                .booker(User.builder().id(1L).build())
                .status(BookingStatus.WAITING)
                .build();

        BookingItemDto bookingItemDto = BookingItemDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .itemId(1L)
                .bookerId(1L)
                .status(BookingStatus.WAITING)
                .build();

        BookingItemDto result = bookingMapper.toBookingItemDto(booking);

        assertEquals(bookingItemDto, result);
    }
}