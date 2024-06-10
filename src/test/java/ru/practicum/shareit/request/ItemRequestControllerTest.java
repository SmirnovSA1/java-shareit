package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestService itemRequestService;
    private LocalDateTime created;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDtoResponse itemRequestDtoResponse;

    @BeforeEach
    void setUp() {
        created = LocalDateTime.now();
        itemRequestDto = createItemRequestDto();
        itemRequestDtoResponse = createItemResponseDtoResponse();
    }

    @SneakyThrows
    @Test
    void addItemRequest() {
        when(itemRequestService.addItemRequest(any(Long.class), any())).thenReturn(itemRequestDtoResponse);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.description").value(itemRequestDtoResponse.getDescription()))
                .andExpect(jsonPath("$.created").isNotEmpty());
    }

    @SneakyThrows
    @Test
    void getItemRequestListForUser() {
        when(itemRequestService.getItemRequestListForUser(any(Long.class))).thenReturn(List.of(itemRequestDtoResponse));

        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].description").value(itemRequestDtoResponse.getDescription()))
                .andExpect(jsonPath("$[0].created").isNotEmpty());
    }

    @SneakyThrows
    @Test
    void getAllItemRequests() {
        when(itemRequestService.getAllItemRequests(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(itemRequestDtoResponse));

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].description").value(itemRequestDtoResponse.getDescription()))
                .andExpect(jsonPath("$[0].created").isNotEmpty());
    }

    @SneakyThrows
    @Test
    void getItemRequestById() {
        when(itemRequestService.getItemRequestById(any(Long.class), any(Long.class))).thenReturn(itemRequestDtoResponse);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.description").value(itemRequestDtoResponse.getDescription()))
                .andExpect(jsonPath("$.created").isNotEmpty());
    }

    private ItemRequestDto createItemRequestDto() {
        return ItemRequestDto.builder()
                .description("Описание")
                .build();
    }

    private ItemRequestDtoResponse createItemResponseDtoResponse() {
        return ItemRequestDtoResponse.builder()
                .id(1L)
                .description("Описание")
                .created(created)
                .build();
    }
}