package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    @Query("select b from Booking b " +
            "join Item i on i.id = b.item.id " +
            "where i.owner.id = ?1 " +
            "order by b.start desc")
    List<Booking> findAllBookingsForUserItemsOrderByStartDesc(Long ownerId);

    @Query("select b from Booking b " +
            "join Item i on i.id = b.item.id " +
            "where i.owner.id = ?1 " +
            "and b.start < ?2 " +
            "and b.end > ?3 " +
            "order by b.start desc")
    List<Booking> findAllBookingsForUserItemsWhereStartBeforeAndEndAfterOrderByStartDesc(
            Long ownerId, LocalDateTime start, LocalDateTime end);

    @Query("select b from Booking b " +
            "join Item i on i.id = b.item.id " +
            "where i.owner.id = ?1 " +
            "and b.end < ?2 " +
            "order by b.start desc")
    List<Booking> findAllBookingsForUserItemsWhereEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime start);

    @Query("select b from Booking b " +
            "join Item i on i.id = b.item.id " +
            "where i.owner.id = ?1 " +
            "and b.start > ?2 " +
            "order by b.start desc")
    List<Booking> findAllBookingsForUserItemsWhereStartAfterOrderByStartDesc(Long ownerId, LocalDateTime start);

    @Query("select b from Booking b " +
            "join Item i on i.id = b.item.id " +
            "where i.owner.id = ?1 " +
            "and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> findAllBookingsForUserItemsByStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    List<Booking> findAllByItemIdOrderByEndDesc(Long itemId);

    List<Booking> findAllByItemInOrderByEndDesc(List<Item> items);
}
