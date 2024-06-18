package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    private ItemDto itemDto;
    private ItemDto itemDtoResponse;
    private ItemUpdatedDto itemUpdatedDto;
    private ItemDtoInfo itemDtoInfo;
    private UserDto userDto;
    private CommentDto commentDto;
    private CommentDtoResponse commentDtoResponse;
    private LocalDateTime createdComment;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .name("Item")
                .description("Описание")
                .available(true)
                .build();
        itemDtoResponse = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Описание")
                .available(true)
                .build();
        itemUpdatedDto = ItemUpdatedDto.builder()
                .id(1L)
                .name("Item Updated")
                .description("Новое описание")
                .available(true)
                .build();
        itemDtoInfo = ItemDtoInfo.builder()
                .id(1L)
                .name("Item Updated")
                .description("Новое описание")
                .available(true)
                .build();
        userDto = UserDto.builder()
                .id(1L)
                .name("User")
                .email("user@test.ru")
                .build();
        commentDto = CommentDto.builder()
                .id(1L)
                .text("Some text")
                .item(itemDto)
                .author(userDto)
                .build();
        createdComment = LocalDateTime.now();
        commentDtoResponse = CommentDtoResponse.builder()
                .id(1L)
                .text("Some text")
                .authorName(userDto.getName())
                .created(createdComment)
                .build();
    }

    @SneakyThrows
    @Test
    void createItem() {
        when(itemService.createItem(any(Long.class), any(ItemDto.class))).thenReturn(itemDtoResponse);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(itemDtoResponse.getId()))
                .andExpect(jsonPath("$.name").value(itemDtoResponse.getName()))
                .andExpect(jsonPath("$.description").value(itemDtoResponse.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDtoResponse.getAvailable()));
    }

    @SneakyThrows
    @Test
    void updateItem() {
        when(itemService.updateItem(any(Long.class), any(Long.class), any(ItemUpdatedDto.class))).thenReturn(itemDtoInfo);

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemUpdatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDtoInfo.getId()))
                .andExpect(jsonPath("$.name").value(itemDtoInfo.getName()))
                .andExpect(jsonPath("$.description").value(itemDtoInfo.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDtoInfo.getAvailable()));
    }

    @SneakyThrows
    @Test
    void getItemById() {
        when(itemService.getItemById(any(Long.class), any(Long.class)))
                .thenReturn(ItemDtoInfo.builder()
                        .id(1L)
                        .name("Item")
                        .description("Описание")
                        .available(true)
                        .build());

        mockMvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Item"))
                .andExpect(jsonPath("$.description").value("Описание"))
                .andExpect(jsonPath("$.available").value("true"));
    }

    @SneakyThrows
    @Test
    void getItemsByOwner() {
        ItemDtoInfo itemDtoInfo2 = ItemDtoInfo.builder()
                .id(1L)
                .name("Item 2")
                .description("Описание 2")
                .available(false)
                .build();

        when(itemService.getItemsByOwner(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(itemDtoInfo, itemDtoInfo2));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(itemDtoInfo.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDtoInfo.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDtoInfo.getAvailable()))
                .andExpect(jsonPath("$[1].name").value(itemDtoInfo2.getName()))
                .andExpect(jsonPath("$[1].description").value(itemDtoInfo2.getDescription()))
                .andExpect(jsonPath("$[1].available").value(itemDtoInfo2.getAvailable()));
    }

    @SneakyThrows
    @Test
    void getItemByText() {
        when(itemService.getItemByText(any(Long.class), any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(itemDtoInfo));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "uPDated")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(itemDtoInfo.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDtoInfo.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDtoInfo.getAvailable()));
    }

    @SneakyThrows
    @Test
    void addComment() {
        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentDtoResponse);

        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(commentDtoResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.text").value(commentDtoResponse.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDtoResponse.getAuthorName()));
    }
}