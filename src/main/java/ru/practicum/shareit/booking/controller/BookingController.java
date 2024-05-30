package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.UnknownStateException;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDtoExtended createBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                            @Valid @RequestBody BookingDto bookingDto) {
        log.info("Создание бронирования");
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDtoExtended confirmOrRejectBooing(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                            @PathVariable(name = "bookingId") Long bookingId,
                                            @RequestParam(name = "approved", required = true) Boolean approved) {
        log.info("Попытка подтверждения бронирования");
        return bookingService.confirmOrRejectBooing(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDtoExtended getBookingById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                     @PathVariable(name = "bookingId") Long bookingId) {
        log.info("Получения бронирования");
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDtoExtended> getUserBookings(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                    @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение бронирований пользователя");

        try {
            return bookingService.getUserBookings(userId, BookingState.valueOf(state));
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException(String.format("Неизвестное состояние: %s", state));
        }
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDtoExtended> getByItemOwnerBookings(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                           @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение бронирований владельца вещи");

        try {
            return bookingService.getByItemOwnerBookings(userId, BookingState.valueOf(state));
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException(String.format("Неизвестное состояние: %s", state));
        }
    }
}