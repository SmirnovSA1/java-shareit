package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.item.dto.ItemUpdatedDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "requestId", source = "item.request.id")
    ItemDto toItemDto(Item item);

    @Mapping(target = "owner", ignore = true)
    Item toItem(ItemDto item);

    @Mapping(target = "owner", ignore = true)
    Item toItemFromUpdatedDto(ItemUpdatedDto item);

    @Mapping(target = "requestId", source = "item.request.id")
    ItemDtoInfo toItemDtoInfo(Item item);

    List<ItemDto> toListItemDto(List<Item> items);

    List<ItemDtoInfo> toListItemDtoInfo(List<Item> items);
}
