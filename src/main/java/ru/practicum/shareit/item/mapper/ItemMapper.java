package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.item.dto.ItemUpdatedDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toItemDto(Item item);

    @Mapping(target = "owner", ignore = true)
    Item toItem(ItemDto item);

    @Mapping(target = "owner", ignore = true)
    Item toItemFromUpdatedDto(ItemUpdatedDto item);

    ItemDtoInfo toItemDtoInfo(Item item);
}
