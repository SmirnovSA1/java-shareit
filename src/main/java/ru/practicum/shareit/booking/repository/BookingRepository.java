package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long bookerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(
            Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBefore(Long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfter(Long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findAllBookingsByItem_OwnerId(Long ownerId, Pageable pageable);

    List<Booking> findAllBookingsByItem_OwnerIdAndStartBeforeAndEndAfter(
            Long ownerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findAllBookingsByItem_OwnerIdAndEndBefore(Long ownerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllBookingsByItem_OwnerIdAndStartAfter(Long ownerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllBookingsByItem_OwnerIdAndStatus(Long ownerId, BookingStatus status, Pageable pageable);

    List<Booking> findAllByItemIdOrderByEndDesc(Long itemId);

    List<Booking> findAllByItemInOrderByEndDesc(List<Item> items);
}
