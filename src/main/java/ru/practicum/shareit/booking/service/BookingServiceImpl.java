package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingDtoExtended createBooking(Long userId, BookingDto bookingDto) {
        Booking bookingFromDto = bookingMapper.toBooking(bookingDto);
        User user = userMapper.toUser(userService.getUserById(userId));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format("Вещь по id %d не найдена", bookingDto.getItemId())));

        bookingFromDto.setItem(item);
        bookingFromDto.setBooker(user);

        if (bookingFromDto.getStart().isEqual(bookingFromDto.getEnd())) {
            throw new BadRequestException("Дата начала бронирование не может быть равна дате окончания!");
        }

        if (bookingFromDto.getEnd().isBefore(bookingFromDto.getStart())) {
            throw new BadRequestException("Дата начала должна быть раньше даты окончания!");
        }

        if (bookingFromDto.getItem().getOwner().getId().equals(userId)) {
            throw new WrongItemOwnerException(
                    String.format("Пользователь с id %d не может быть владельцем этой вещи", userId));
        }

        if (!bookingFromDto.getItem().getAvailable()) throw new BadRequestException("Вещь сейчас недоступна");

        Booking booking = bookingRepository.save(bookingFromDto);
        return bookingMapper.toBookingDtoExtended(booking);
    }

    @Transactional
    @Override
    public BookingDtoExtended confirmOrRejectBooing(Long userId, Long bookingId, Boolean approved) {
        Booking foundBooking = bookingMapper.toBookingFromExtended(getBookingById(userId, bookingId));

        if (!foundBooking.getItem().getOwner().getId().equals(userId)) {
            throw new WrongItemOwnerException(
                    String.format("Владелец вещи не совпадает с переданным id %d", userId));
        }

        if (foundBooking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new AlreadyExistException(String.format("Бронирование %d уже было подтверждено ранее.", bookingId));
        }

        foundBooking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return bookingMapper.toBookingDtoExtended(bookingRepository.save(foundBooking));
    }

    @Override
    public BookingDtoExtended getBookingById(Long userId, Long bookingId) {
        Booking foundBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!foundBooking.getBooker().getId().equals(userId) && !foundBooking.getItem().getOwner().getId().equals(userId)) {
            throw new WrongItemOwnerException(
                    String.format("Пользователь с id %d не является владельцем или инициатором бронирования", userId));
        }

        return bookingMapper.toBookingDtoExtended(foundBooking);
    }

    @Override
    public List<BookingDtoExtended> getUserBookings(Long bookerId, BookingState state) {
        userService.getUserById(bookerId);

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case CURRENT:
                bookings = bookingRepository.
                        findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, now, now);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, now);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
                break;
            default:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
        }

        return bookings.stream()
                .map(bookingMapper::toBookingDtoExtended)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoExtended> getByItemOwnerBookings(Long userId, BookingState state) {
        User owner = userMapper.toUser(userService.getUserById(userId));

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case CURRENT:
                bookings = bookingRepository.
                        findAllBookingsForUserItemsWhereStartBeforeAndEndAfterOrderByStartDesc(
                                owner.getId(), now, now);
                break;
            case PAST:
                bookings = bookingRepository.
                        findAllBookingsForUserItemsWhereEndBeforeOrderByStartDesc(owner.getId(), now);
                break;
            case FUTURE:
                bookings = bookingRepository.
                        findAllBookingsForUserItemsWhereStartAfterOrderByStartDesc(owner.getId(), now);
                break;
            case WAITING:
                bookings = bookingRepository.
                        findAllBookingsForUserItemsByStatusOrderByStartDesc(owner.getId(), BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.
                        findAllBookingsForUserItemsByStatusOrderByStartDesc(owner.getId(), BookingStatus.REJECTED);
                break;
            default:
                bookings = bookingRepository.findAllBookingsForUserItemsOrderByStartDesc(owner.getId());
        }

        return bookings.stream().
                map(bookingMapper::toBookingDtoExtended).
                collect(Collectors.toList());
    }
}
