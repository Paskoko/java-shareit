package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Interface for items storage
 */
public interface ItemStorage {

    /**
     * Add item to the storage
     *
     * @param item to add
     * @return added item
     */
    Item addItem(Item item);

    /**
     * Update item in the storage
     *
     * @param item to update
     * @return updated item
     */
    Item updateItem(Item item);

    /**
     * Get item by the id
     *
     * @param id of the item
     * @return item
     */
    Item getItemById(int id);

    /**
     * Get list of all user's items
     *
     * @param userId of user
     * @return list of all items
     */
    List<Item> getAllItems(int userId);


    /**
     * Get list of searched items
     *
     * @param request for item
     * @return list of items
     */
    List<Item> searchItems(String request);

}
