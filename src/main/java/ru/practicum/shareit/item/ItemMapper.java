package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class mapper for item object
 */
public class ItemMapper {
    /**
     * Transform item to itemDto object
     *
     * @param item to transform
     * @return itemDto object
     */
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwnerId())
                .available(item.getIsAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    /**
     * Transform itemDto to item object
     *
     * @param itemDto to transform
     * @return item object
     */
    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .ownerId(itemDto.getOwnerId())
                .isAvailable(itemDto.getAvailable())
                .request(ItemRequest.builder()
                        .requestId(itemDto.getRequestId())
                        .build())
                .build();
    }


    /**
     * Transform list of item to list of itemDto objects
     *
     * @param itemList to transform
     * @return list of itemDto objects
     */
    public static List<ItemDto> toListUserDto(List<Item> itemList) {
        return itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
