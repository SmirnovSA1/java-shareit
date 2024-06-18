package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.item.dto.ItemUpdatedDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class ItemSerializationTest {
    @Autowired
    private JacksonTester<ItemDto> toJsonTesterItemDto;
    @Autowired
    private JacksonTester<ItemDto> toDtoTesterItemDto;
    @Autowired
    private JacksonTester<ItemDtoInfo> toJsonTesterItemDtoInfo;
    @Autowired
    private JacksonTester<ItemDtoInfo> toDtoTesterItemDtoInfo;
    @Autowired
    private JacksonTester<ItemUpdatedDto> toJsonTesterItemUpdatedDto;
    @Autowired
    private JacksonTester<ItemUpdatedDto> toDtoTesterItemUpdatedDto;

    @Test
    void dtoSerializationTest() throws IOException {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Описание")
                .available(true)
                .build();

        JsonContent<ItemDto> result1 = toJsonTesterItemDto.write(itemDto);

        assertThat(result1).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result1).extractingJsonPathStringValue("$.name").isEqualTo("Item");
        assertThat(result1).extractingJsonPathStringValue("$.description").isEqualTo("Описание");
        assertThat(result1).extractingJsonPathBooleanValue("$.available").isEqualTo(true);

        ItemDtoInfo itemDtoInfo = ItemDtoInfo.builder()
                .id(2L)
                .name("Item info")
                .description("Описание инфо")
                .available(true)
                .build();

        JsonContent<ItemDtoInfo> result2 = toJsonTesterItemDtoInfo.write(itemDtoInfo);

        assertThat(result2).extractingJsonPathNumberValue("$.id").isEqualTo(2);
        assertThat(result2).extractingJsonPathStringValue("$.name").isEqualTo("Item info");
        assertThat(result2).extractingJsonPathStringValue("$.description").isEqualTo("Описание инфо");
        assertThat(result2).extractingJsonPathBooleanValue("$.available").isEqualTo(true);

        ItemUpdatedDto itemUpdatedDto = ItemUpdatedDto.builder()
                .id(3L)
                .name("Item updated")
                .description("Описание обновления")
                .available(false)
                .build();

        JsonContent<ItemUpdatedDto> result3 = toJsonTesterItemUpdatedDto.write(itemUpdatedDto);

        assertThat(result3).extractingJsonPathNumberValue("$.id").isEqualTo(3);
        assertThat(result3).extractingJsonPathStringValue("$.name").isEqualTo("Item updated");
        assertThat(result3).extractingJsonPathStringValue("$.description").isEqualTo("Описание обновления");
        assertThat(result3).extractingJsonPathBooleanValue("$.available").isEqualTo(false);
    }

    @Test
    void dtoDeserializationTest() throws IOException {
        String json1 = "{ \"id\":\"1\", \"name\":\"Item\", \"description\":\"Описание\", \"available\":\"true\"}";
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Описание")
                .available(true)
                .build();

        ItemDto result1 = toDtoTesterItemDto.parseObject(json1);

        assertEquals(itemDto, result1);

        String json2 = "{ \"id\":\"2\", \"name\":\"Item info\", \"description\":\"Описание инфо\", \"available\":\"true\"}";
        ItemDtoInfo itemDtoInfo = ItemDtoInfo.builder()
                .id(2L)
                .name("Item info")
                .description("Описание инфо")
                .available(true)
                .build();

        ItemDtoInfo result2 = toDtoTesterItemDtoInfo.parseObject(json2);

        assertEquals(itemDtoInfo, result2);

        String json3 = "{ \"id\":\"3\", \"name\":\"Item updated\", " +
                "\"description\":\"Описание обновления\", \"available\":\"false\"}";
        ItemUpdatedDto itemUpdatedDto = ItemUpdatedDto.builder()
                .id(3L)
                .name("Item updated")
                .description("Описание обновления")
                .available(false)
                .build();

        ItemUpdatedDto result3 = toDtoTesterItemUpdatedDto.parseObject(json3);

        assertEquals(itemUpdatedDto, result3);
    }
}
