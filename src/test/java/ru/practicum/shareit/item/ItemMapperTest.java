package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemMapperTest {


    Item item = Item.builder()
            .id(1)
            .name("test item")
            .description("test")
            .ownerId(1)
            .isAvailable(true)
            .requestId(1)
            .commentList(new ArrayList<>())
            .build();


    @Test
    void toItemDto() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .available(true)
                .requestId(1)
                .comments(new ArrayList<>())
                .build();

        ItemDto result = ItemMapper.toItemDto(item);

        assertNotNull(result);
        assertEquals(itemDto, result);
    }

    @Test
    void toItemBookingDto() {
        ItemBookingDto itemBookingDto = ItemBookingDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .available(true)
                .requestId(1)
                .build();

        ItemBookingDto result = ItemMapper.toItemBookingDto(item);

        assertNotNull(result);
        assertEquals(itemBookingDto, result);
    }

    @Test
    void toItemRequestDto() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .available(true)
                .requestId(1)
                .build();

        ItemRequestDto result = ItemMapper.toItemRequestDto(item);

        assertNotNull(result);
        assertEquals(itemRequestDto, result);
    }

    @Test
    void toItem() {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .available(true)
                .requestId(1)
                .comments(new ArrayList<>())
                .build();

        Item result = ItemMapper.toItem(itemDto);

        assertNotNull(result);
        assertEquals(item, result);
    }

    @Test
    void testToItem() {
        ItemBookingDto itemBookingDto = ItemBookingDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .available(true)
                .requestId(1)
                .build();
        Item anotherItem = Item.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .isAvailable(true)
                .requestId(1)
                .build();

        Item result = ItemMapper.toItem(itemBookingDto);

        assertNotNull(result);
        assertEquals(anotherItem, result);
    }

    @Test
    void toListItemDto() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .available(true)
                .requestId(1)
                .comments(new ArrayList<>())
                .build();
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);

        List<ItemDto> result = ItemMapper.toListItemDto(itemList);

        assertNotNull(result);
        assertEquals(itemDtoList, result);
    }

    @Test
    void toListItemBookingDto() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        ItemBookingDto itemBookingDto = ItemBookingDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .available(true)
                .requestId(1)
                .build();
        List<ItemBookingDto> itemBookingDtoList = new ArrayList<>();
        itemBookingDtoList.add(itemBookingDto);

        List<ItemBookingDto> result = ItemMapper.toListItemBookingDto(itemList);

        assertNotNull(result);
        assertEquals(itemBookingDtoList, result);
    }

    @Test
    void toListItemRequestDto() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .available(true)
                .requestId(1)
                .build();
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        itemRequestDtoList.add(itemRequestDto);

        List<ItemRequestDto> result = ItemMapper.toListItemRequestDto(itemList);

        assertNotNull(result);
        assertEquals(itemRequestDtoList, result);
    }
}