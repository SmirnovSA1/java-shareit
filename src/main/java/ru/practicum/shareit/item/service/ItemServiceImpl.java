package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    @Override
    public ItemDto createItem(Long userId, ItemDto newItemDto) {
        User foundUser = userMapper.toUser(userService.getUserById(userId));
        Item itemFromDto = itemMapper.toItem(newItemDto);
        itemFromDto.setOwner(foundUser);
        return itemMapper.toItemDto(itemRepository.save(itemFromDto));
    }

    @Override
    public ItemDtoInfo getItemById(Long userId, Long itemId) {
        Item foundItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь по id %d не найдена", itemId)));

        List<Booking> bookings = bookingRepository.findAllByItemIdOrderByEndDesc(foundItem.getId());
        List<Comment> comments = commentRepository.findAllByItemId(foundItem.getId());
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookingsForItem = bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart))
                .collect(Collectors.toList());

        Optional<Booking> next = bookingsForItem.stream()
                .filter(b -> b.getStatus().equals(BookingStatus.APPROVED) &&
                        Objects.equals(b.getItem().getOwner().getId(), userId) &&
                        Objects.equals(b.getItem().getId(), foundItem.getId()) &&
                        b.getStart().isAfter(now) || b.getStart().isEqual(now))
                .findFirst();

        Optional<Booking> last = bookings.stream()
                .filter(b -> b.getStatus().equals(BookingStatus.APPROVED) &&
                        Objects.equals(b.getItem().getOwner().getId(), userId) &&
                        Objects.equals(b.getItem().getId(), foundItem.getId()) &&
                        b.getStart().isBefore(now))
                .findFirst();

        ItemDtoInfo itemDtoInfo = itemMapper.toItemDtoInfo(foundItem);
        last.ifPresent(booking -> itemDtoInfo.setLastBooking(bookingMapper.toBookingItemDto(last.get())));
        next.ifPresent(booking -> itemDtoInfo.setNextBooking(bookingMapper.toBookingItemDto(next.get())));

        if (comments != null) {
            itemDtoInfo.setComments(comments.stream()
                    .map(commentMapper::toCommentDtoResponse)
                    .collect(Collectors.toList()));
        }

        return itemDtoInfo;
    }

    @Override
    public List<ItemDtoInfo> getItemsByOwner(Long userId) {
        List<Item> itemsByOwner = itemRepository.findByOwnerId(userId);
        List<Booking> bookings = bookingRepository.findAllByItemInOrderByEndDesc(itemsByOwner);
        List<Comment> comments = commentRepository.findAllByItemIn(itemsByOwner);

        return itemsByOwner.stream()
                .map(item -> {
                    LocalDateTime now = LocalDateTime.now();

                    List<Booking> bookingsForItem = bookings.stream()
                            .sorted(Comparator.comparing(Booking::getStart))
                            .collect(Collectors.toList());

                    Optional<Booking> next = bookingsForItem.stream()
                            .filter(b -> b.getStatus().equals(BookingStatus.APPROVED) &&
                                    Objects.equals(b.getItem().getOwner().getId(), userId) &&
                                    Objects.equals(b.getItem().getId(), item.getId()) &&
                                    b.getStart().isAfter(now) || b.getStart().isEqual(now))
                            .findFirst();

                    Optional<Booking> last = bookings.stream()
                            .filter(b -> b.getStatus().equals(BookingStatus.APPROVED) &&
                                    Objects.equals(b.getItem().getOwner().getId(), userId) &&
                                    Objects.equals(b.getItem().getId(), item.getId()) &&
                                    b.getStart().isBefore(now))
                            .findFirst();

                    ItemDtoInfo itemDtoInfo = itemMapper.toItemDtoInfo(item);
                    last.ifPresent(booking -> itemDtoInfo.setLastBooking(bookingMapper.toBookingItemDto(last.get())));
                    next.ifPresent(booking -> itemDtoInfo.setNextBooking(bookingMapper.toBookingItemDto(next.get())));

                    if (comments != null) {
                        itemDtoInfo.setComments(comments.stream()
                                .map(commentMapper::toCommentDtoResponse)
                                .collect(Collectors.toList()));
                    }

                    return itemDtoInfo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoInfo> getItemByText(Long userId, String text) {
        userService.getUserById(userId);

        if (text.trim().isBlank()) return new ArrayList<>();

        return itemRepository.findByNameContainingIgnoreCase(text).stream()
                .map(itemMapper::toItemDtoInfo)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ItemDtoInfo updateItem(Long userId, Long itemId, ItemUpdatedDto updatedItemDto) {
        userService.getUserById(userId);

        Item itemFromDto = itemMapper.toItemFromUpdatedDto(updatedItemDto);
        Item foundItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь по id %d не найдена", itemId)));

        if (itemFromDto.getName() != null) {
            foundItem.setName(itemFromDto.getName());
        }

        if (itemFromDto.getDescription() != null) {
            foundItem.setDescription(itemFromDto.getDescription());
        }

        if (itemFromDto.getAvailable() != null) {
            foundItem.setAvailable(itemFromDto.getAvailable());
        }

        return itemMapper.toItemDtoInfo(itemRepository.save(foundItem));
    }

    @Override
    public CommentDtoResponse addComment(Long userId, Long itemId, CommentDto commentDto) {
        User author = userMapper.toUser(userService.getUserById(userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь по id %d не найдена", itemId)));

        List<Booking> bookingsForItem = bookingRepository.findAllByItemIdOrderByEndDesc(item.getId());

        bookingsForItem.stream()
                .filter(b -> b.getBooker().getId().equals(author.getId()) &&
                        b.getEnd().isBefore(LocalDateTime.now()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(
                        String.format("Пользователь с id %d не брал вещь с id %d в аренду", userId, item.getId())));

        Comment comment = commentMapper.toComment(commentDto);
        comment.setAuthor(author);
        comment.setItem(item);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentDtoResponse(savedComment);
    }
}
