package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;

import java.util.List;

public interface BookingService {

    BookingDtoExtended createBooking(Long userId, BookingDto bookingDto);

    BookingDtoExtended confirmOrRejectBooing(Long userId, Long bookingId, Boolean approved);

    BookingDtoExtended getBookingById(Long userId, Long bookingId);

    List<BookingDtoExtended> getUserBookings(Long userId, BookingState state);

    List<BookingDtoExtended> getByItemOwnerBookings(Long userId, BookingState state);
}
