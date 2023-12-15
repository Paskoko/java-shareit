package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * Interface for item service
 */
public interface ItemService {

    /**
     * Add new item with validation
     *
     * @param itemDto to add
     * @param userId  of owner
     * @return added item
     */
    ItemDto createItem(ItemDto itemDto, String userId);

    /**
     * Get item by id with validation
     *
     * @param itemId of item
     * @param userId of owner
     * @return item
     */
    ItemDto getItemById(int itemId, String userId);

    /**
     * Get list of all user's items
     *
     * @param userId of owner
     * @return list of user's items
     */
    List<ItemDto> getAllItems(String userId);

    /**
     * Update item with validation
     *
     * @param itemDto item to update
     * @param itemId  of item
     * @param userId  of owner
     * @return updated item
     */
    ItemDto updateItem(ItemDto itemDto, int itemId, String userId);

    /**
     * Search for items
     *
     * @param text   to search
     * @param userId of user
     * @return list of found items
     */
    List<ItemDto> searchItems(String text, String userId);
}
