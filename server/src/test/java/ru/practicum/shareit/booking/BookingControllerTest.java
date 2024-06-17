package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExtended;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingDto bookingDto;
    private BookingDtoExtended bookingDtoExtended;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now().plusHours(1);
        end = start.plusDays(1);
        bookingDto = BookingDto.builder()
                .start(start)
                .end(end)
                .itemId(1L)
                .booker(1L)
                .status(BookingStatus.WAITING)
                .build();
        bookingDtoExtended = BookingDtoExtended.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(ItemDto.builder().id(1L).name("item").description("описание").available(true).build())
                .booker(UserDto.builder().id(1L).name("User").email("userEmail@test.ru").build())
                .status(BookingStatus.WAITING)
                .build();
    }

    @SneakyThrows
    @Test
    void createBooking() {
        when(bookingService.createBooking(1L, bookingDto)).thenReturn(bookingDtoExtended);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.item.id").value(bookingDtoExtended.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingDtoExtended.getItem().getName()))
                .andExpect(jsonPath("$.item.description").value(bookingDtoExtended.getItem().getDescription()))
                .andExpect(jsonPath("$.item.available").value(bookingDtoExtended.getItem().getAvailable()))
                .andExpect(jsonPath("$.booker.id").value(bookingDtoExtended.getBooker().getId()))
                .andExpect(jsonPath("$.booker.name").value(bookingDtoExtended.getBooker().getName()))
                .andExpect(jsonPath("$.booker.email").value(bookingDtoExtended.getBooker().getEmail()))
                .andExpect(jsonPath("$.status").value(bookingDtoExtended.getStatus().toString()));
    }

    @SneakyThrows
    @Test
    void confirmOrRejectBooing() {
        when(bookingService.confirmOrRejectBooking(any(Long.class), any(Long.class), any(Boolean.class)))
                .thenReturn(bookingDtoExtended);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.item.id").value(bookingDtoExtended.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingDtoExtended.getItem().getName()))
                .andExpect(jsonPath("$.item.description").value(bookingDtoExtended.getItem().getDescription()))
                .andExpect(jsonPath("$.item.available").value(bookingDtoExtended.getItem().getAvailable()))
                .andExpect(jsonPath("$.booker.id").value(bookingDtoExtended.getBooker().getId()))
                .andExpect(jsonPath("$.booker.name").value(bookingDtoExtended.getBooker().getName()))
                .andExpect(jsonPath("$.booker.email").value(bookingDtoExtended.getBooker().getEmail()))
                .andExpect(jsonPath("$.status").value(bookingDtoExtended.getStatus().toString()));
    }

    @SneakyThrows
    @Test
    void getBookingById() {
        when(bookingService.getBookingById(any(Long.class), any(Long.class))).thenReturn(bookingDtoExtended);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.item.id").value(bookingDtoExtended.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingDtoExtended.getItem().getName()))
                .andExpect(jsonPath("$.item.description").value(bookingDtoExtended.getItem().getDescription()))
                .andExpect(jsonPath("$.item.available").value(bookingDtoExtended.getItem().getAvailable()))
                .andExpect(jsonPath("$.booker.id").value(bookingDtoExtended.getBooker().getId()))
                .andExpect(jsonPath("$.booker.name").value(bookingDtoExtended.getBooker().getName()))
                .andExpect(jsonPath("$.booker.email").value(bookingDtoExtended.getBooker().getEmail()))
                .andExpect(jsonPath("$.status").value(bookingDtoExtended.getStatus().toString()));
    }

    @SneakyThrows
    @Test
    void getUserBookings() {
        List<BookingDtoExtended> bookings = List.of(bookingDtoExtended);

        when(bookingService.getUserBookings(any(Long.class), anyString(), anyInt(), anyInt()))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].start").isNotEmpty())
                .andExpect(jsonPath("$[0].end").isNotEmpty())
                .andExpect(jsonPath("$[0].item.id").value(bookingDtoExtended.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDtoExtended.getItem().getName()))
                .andExpect(jsonPath("$[0].item.description").value(bookingDtoExtended.getItem().getDescription()))
                .andExpect(jsonPath("$[0].item.available").value(bookingDtoExtended.getItem().getAvailable()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDtoExtended.getBooker().getId()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDtoExtended.getBooker().getName()))
                .andExpect(jsonPath("$[0].booker.email").value(bookingDtoExtended.getBooker().getEmail()))
                .andExpect(jsonPath("$[0].status").value(bookingDtoExtended.getStatus().toString()));
    }

    @SneakyThrows
    @Test
    void getByItemOwnerBookings() {
        List<BookingDtoExtended> bookings = List.of(bookingDtoExtended);

        when(bookingService.getByItemOwnerBookings(any(Long.class), anyString(), anyInt(), anyInt()))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].start").isNotEmpty())
                .andExpect(jsonPath("$[0].end").isNotEmpty())
                .andExpect(jsonPath("$[0].item.id").value(bookingDtoExtended.getItem().getId()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDtoExtended.getItem().getName()))
                .andExpect(jsonPath("$[0].item.description").value(bookingDtoExtended.getItem().getDescription()))
                .andExpect(jsonPath("$[0].item.available").value(bookingDtoExtended.getItem().getAvailable()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDtoExtended.getBooker().getId()))
                .andExpect(jsonPath("$[0].booker.name").value(bookingDtoExtended.getBooker().getName()))
                .andExpect(jsonPath("$[0].booker.email").value(bookingDtoExtended.getBooker().getEmail()))
                .andExpect(jsonPath("$[0].status").value(bookingDtoExtended.getStatus().toString()));
    }
}