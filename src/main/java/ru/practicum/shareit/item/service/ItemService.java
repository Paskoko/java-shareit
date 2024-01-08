package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
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
    ItemBookingDto getItemById(int itemId, String userId);

    /**
     * Get list of all user's items
     *
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param userId of owner
     * @return list of user's items
     */
    List<ItemBookingDto> getAllItems(Integer from, Integer size, String userId);

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
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param userId of user
     * @return list of found items
     */
    List<ItemDto> searchItems(String text, Integer from, Integer size, String userId);

    /**
     * Add comment to the item
     *
     * @param commentDto to add
     * @param itemId     of item
     * @param userId     of user
     * @return comment
     */
    CommentDto addComment(CommentDto commentDto, int itemId, String userId);
}
