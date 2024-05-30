package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;
import ru.practicum.shareit.booking.dto.BookingItemDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "booker", ignore = true)
    @Mapping(target = "item", ignore = true)
    Booking toBooking(BookingDto bookingDto);

    Booking toBookingFromExtended(BookingDtoExtended bookingDtoExt);

    BookingDtoExtended toBookingDtoExtended(Booking booking);

    @Mapping(target = "itemId", source = "booking.item.id")
    @Mapping(target = "bookerId", source = "booking.booker.id")
    BookingItemDto toBookingItemDto(Booking booking);
}
