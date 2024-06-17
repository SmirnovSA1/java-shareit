package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.item.dto.ItemUpdatedDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ItemMapperTest {
    @Autowired
    private ItemMapper itemMapper;

    @Test
    void toItemDto() {
        Item item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Описание")
                .available(true)
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

        ItemDto result = itemMapper.toItemDto(item);

        assertEquals(itemDto, result);
    }

    @Test
    void toItem() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Описание")
                .available(true)
                .build();

        Item item = Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();

        Item result = itemMapper.toItem(itemDto);

        assertEquals(item, result);
    }

    @Test
    void toItemFromUpdatedDto() {
        ItemUpdatedDto itemUpdatedDto = ItemUpdatedDto.builder()
                .id(1L)
                .name("Item")
                .description("Описание")
                .available(true)
                .build();

        Item item = Item.builder()
                .id(itemUpdatedDto.getId())
                .name(itemUpdatedDto.getName())
                .description(itemUpdatedDto.getDescription())
                .available(itemUpdatedDto.getAvailable())
                .build();

        Item result = itemMapper.toItemFromUpdatedDto(itemUpdatedDto);

        assertEquals(item, result);
    }

    @Test
    void toItemDtoInfo() {
        Item item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Описание")
                .available(true)
                .build();

        ItemDtoInfo itemDtoInfo = ItemDtoInfo.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

        ItemDtoInfo result = itemMapper.toItemDtoInfo(item);

        assertEquals(itemDtoInfo, result);
    }

    @Test
    void toListItemDto() {
        Item item1 = Item.builder()
                .id(1L)
                .name("Item 1")
                .description("Описание 1")
                .available(true)
                .build();

        Item item2 = Item.builder()
                .id(2L)
                .name("Item 2")
                .description("Описание 2")
                .available(false)
                .build();

        List<Item> items = List.of(item1, item2);

        ItemDto itemDto1 = ItemDto.builder()
                .id(item1.getId())
                .name(item1.getName())
                .description(item1.getDescription())
                .available(item1.getAvailable())
                .build();

        ItemDto itemDto2 = ItemDto.builder()
                .id(item2.getId())
                .name(item2.getName())
                .description(item2.getDescription())
                .available(item2.getAvailable())
                .build();

        List<ItemDto> itemsDto = List.of(itemDto1, itemDto2);

        List<ItemDto> result = itemMapper.toListItemDto(items);

        assertEquals(itemsDto, result);
        assertEquals(itemsDto.get(0), result.get(0));
        assertEquals(itemsDto.get(1), result.get(1));
    }

    @Test
    void toListItemDtoInfo() {
        Item item1 = Item.builder()
                .id(1L)
                .name("Item 1")
                .description("Описание 1")
                .available(true)
                .build();

        Item item2 = Item.builder()
                .id(2L)
                .name("Item 2")
                .description("Описание 2")
                .available(false)
                .build();

        List<Item> items = List.of(item1, item2);

        ItemDtoInfo itemDtoInfo1 = ItemDtoInfo.builder()
                .id(item1.getId())
                .name(item1.getName())
                .description(item1.getDescription())
                .available(item1.getAvailable())
                .build();

        ItemDtoInfo itemDtoInfo2 = ItemDtoInfo.builder()
                .id(item2.getId())
                .name(item2.getName())
                .description(item2.getDescription())
                .available(item2.getAvailable())
                .build();

        List<ItemDtoInfo> itemsDtoInfo = List.of(itemDtoInfo1, itemDtoInfo2);

        List<ItemDtoInfo> result = itemMapper.toListItemDtoInfo(items);

        assertEquals(itemsDtoInfo, result);
        assertEquals(itemsDtoInfo.get(0), result.get(0));
        assertEquals(itemsDtoInfo.get(1), result.get(1));
    }
}
