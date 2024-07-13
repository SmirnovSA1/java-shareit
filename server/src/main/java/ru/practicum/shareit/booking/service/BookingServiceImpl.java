package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import ru.practicum.shareit.item.mapper.ItemMapper;
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
    private final ItemMapper itemMapper;

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
        BookingDtoExtended result = bookingMapper.toBookingDtoExtended(booking);
        result.setItem(itemMapper.toItemDto(booking.getItem()));
        result.setBooker(userMapper.toUserDto(booking.getBooker()));
        return result;
    }

    @Transactional
    @Override
    public BookingDtoExtended confirmOrRejectBooking(Long userId, Long bookingId, Boolean approved) {
        Booking foundBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));;

        if (!foundBooking.getItem().getOwner().getId().equals(userId)) {
            throw new WrongItemOwnerException(
                    String.format("Владелец вещи не совпадает с переданным id %d", userId));
        }

        if (foundBooking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new AlreadyExistException(String.format("Бронирование %d уже было подтверждено ранее.", bookingId));
        }

        foundBooking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        Booking savedBooking = bookingRepository.save(foundBooking);
        BookingDtoExtended result = bookingMapper.toBookingDtoExtended(savedBooking);
        result.setItem(itemMapper.toItemDto(savedBooking.getItem()));
        result.setBooker(userMapper.toUserDto(savedBooking.getBooker()));
        return result;
    }

    @Override
    public BookingDtoExtended getBookingById(Long userId, Long bookingId) {
        Booking foundBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!foundBooking.getBooker().getId().equals(userId) && !foundBooking.getItem().getOwner().getId().equals(userId)) {
            throw new WrongItemOwnerException(
                    String.format("Пользователь с id %d не является владельцем или инициатором бронирования", userId));
        }

        BookingDtoExtended result = bookingMapper.toBookingDtoExtended(foundBooking);
        result.setItem(itemMapper.toItemDto(foundBooking.getItem()));
        result.setBooker(userMapper.toUserDto(foundBooking.getBooker()));
        return result;
    }

    @Override
    public List<BookingDtoExtended> getUserBookings(Long bookerId, String state, Integer from, Integer size) {
        BookingState stateEnum;

        try {
            stateEnum = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException(String.format("Неизвестное состояние: %s", state));
        }

        if (from < 0 || size <= 0) {
            throw new BadRequestException("Неверно указаны параметры запроса. \n " +
                    "'from' должен быть больше или равен 0, 'size' должен быть больше 0");
        }

        int pageNumber = from / size;

        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("start").descending());

        userService.getUserById(bookerId);

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (stateEnum) {
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(bookerId, now, now, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(bookerId, now, pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(bookerId, now, pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.REJECTED, pageable);
                break;
            default:
                bookings = bookingRepository.findAllByBookerId(bookerId, pageable);
        }

        return bookings.stream().map(booking -> {
                    BookingDtoExtended dto = bookingMapper.toBookingDtoExtended(booking);
                    dto.setItem(itemMapper.toItemDto(booking.getItem()));
                    dto.setBooker(userMapper.toUserDto(booking.getBooker()));
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoExtended> getByItemOwnerBookings(Long userId, String state, Integer from, Integer size) {
        BookingState stateEnum;

        try {
            stateEnum = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException(String.format("Неизвестное состояние: %s", state));
        }

        if (from < 0 || size <= 0) {
            throw new BadRequestException("Неверно указаны параметры запроса. \n " +
                    "'from' должен быть больше или равен 0, 'size' должен быть больше 0");
        }

        int pageNumber = from / size;

        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("start").descending());

        User owner = userMapper.toUser(userService.getUserById(userId));

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (stateEnum) {
            case CURRENT:
                bookings = bookingRepository.findAllBookingsByItem_OwnerIdAndStartBeforeAndEndAfter(owner.getId(), now, now, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findAllBookingsByItem_OwnerIdAndEndBefore(owner.getId(), now, pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllBookingsByItem_OwnerIdAndStartAfter(owner.getId(), now, pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findAllBookingsByItem_OwnerIdAndStatus(owner.getId(), BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllBookingsByItem_OwnerIdAndStatus(owner.getId(), BookingStatus.REJECTED, pageable);
                break;
            default:
                bookings = bookingRepository.findAllBookingsByItem_OwnerId(owner.getId(), pageable);
        }

        return bookings.stream().map(booking -> {
            BookingDtoExtended dto = bookingMapper.toBookingDtoExtended(booking);
            dto.setItem(itemMapper.toItemDto(booking.getItem()));
            dto.setBooker(userMapper.toUserDto(booking.getBooker()));
            return dto;
        }).collect(Collectors.toList());
    }
}
