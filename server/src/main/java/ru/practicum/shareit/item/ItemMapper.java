package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;

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
                .requestId(item.getRequestId())
                .comments(item.getCommentList())
                .build();
    }

    /**
     * Transform item to itemBookingDto object
     *
     * @param item to transform
     * @return itemBookingDto object
     */
    public static ItemBookingDto toItemBookingDto(Item item) {
        return ItemBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwnerId())
                .available(item.getIsAvailable())
                .requestId(item.getRequestId())
                .build();
    }

    /**
     * Transform item to itemRequestDto object
     *
     * @param item to transform
     * @return itemRequestDto object
     */
    public static ItemRequestDto toItemRequestDto(Item item) {
        return ItemRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .requestId(item.getRequestId())
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
                .requestId(itemDto.getRequestId())
                .commentList(itemDto.getComments())
                .build();
    }

    /**
     * Transform itemBookingDto to item object
     *
     * @param itemBookingDto to transform
     * @return item object
     */
    public static Item toItem(ItemBookingDto itemBookingDto) {
        return Item.builder()
                .id(itemBookingDto.getId())
                .name(itemBookingDto.getName())
                .description(itemBookingDto.getDescription())
                .ownerId(itemBookingDto.getOwnerId())
                .isAvailable(itemBookingDto.getAvailable())
                .requestId(itemBookingDto.getRequestId())
                .build();
    }

    /**
     * Transform list of item to list of itemDto objects
     *
     * @param itemList to transform
     * @return list of itemDto objects
     */
    public static List<ItemDto> toListItemDto(List<Item> itemList) {
        return itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    /**
     * Transform list of item to list of itemBookingDto objects
     *
     * @param itemList to transform
     * @return list of itemBookingDto objects
     */
    public static List<ItemBookingDto> toListItemBookingDto(List<Item> itemList) {
        return itemList.stream().map(ItemMapper::toItemBookingDto).collect(Collectors.toList());
    }

    /**
     * Transform list of item to list of itemRequestDto objects
     *
     * @param itemList to transform
     * @return list of itemRequestDto objects
     */
    public static List<ItemRequestDto> toListItemRequestDto(List<Item> itemList) {
        return itemList.stream().map(ItemMapper::toItemRequestDto).collect(Collectors.toList());
    }

}
