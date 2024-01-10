package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemTest {
    Item comparedItem = Item.builder()
            .id(1)
            .name("test item")
            .description("test")
            .ownerId(1)
            .isAvailable(true)
            .requestId(1)
            .commentList(new ArrayList<>())
            .build();

    @Test
    void compareWithNull() {
        Item item = Item.builder()
                .id(1)
                .name(null)
                .description(null)
                .ownerId(0)
                .isAvailable(null)
                .requestId(0)
                .commentList(null)
                .build();

        Item result = comparedItem.compare(item);

        assertEquals(comparedItem, result);
    }

    @Test
    void compareWithFull() {
        Item item = Item.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .isAvailable(true)
                .requestId(1)
                .commentList(new ArrayList<>())
                .build();

        Item result = comparedItem.compare(item);

        assertEquals(comparedItem, result);
    }
}