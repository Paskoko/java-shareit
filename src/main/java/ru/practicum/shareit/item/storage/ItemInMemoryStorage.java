package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.exceptions.ResourceNotFoundException;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class storage for items in memory
 */
@Component("itemInMemoryStorage")
public class ItemInMemoryStorage implements ItemStorage {

    private final HashMap<Integer, Item> items = new HashMap<>();
    private static int id = 0;  // Unique id for item

    /**
     * Add item to the storage
     *
     * @param item to add
     * @return added item
     */
    @Override
    public Item addItem(Item item) {
        if (item.getIsAvailable() == null) {
            throw new ValidationException("No available field!");
        }
        item.setId(++id);
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    /**
     * Update item in the storage
     *
     * @param item to update
     * @return updated item
     */
    @Override
    public Item updateItem(Item item) {
        if (items.containsKey(item.getId())) {
            Item itemFromStorage = items.get(item.getId());
            if (itemFromStorage.getOwnerId() != item.getOwnerId()) {
                throw new ResourceNotFoundException("False owner id!");
            }
            items.put(item.getId(), itemFromStorage.compare(item));
            return items.get(item.getId());
        } else {
            throw new ResourceNotFoundException("No such item!");
        }
    }

    /**
     * Get item by the id
     *
     * @param id of the item
     * @return item
     */
    @Override
    public Item getItemById(int id) {
        if (items.containsKey(id)) {
            return items.get(id);
        } else {
            throw new ResourceNotFoundException("No such item with that id!");
        }
    }

    /**
     * Get list of all user's items
     *
     * @param userId of user
     * @return list of all items
     */
    @Override
    public List<Item> getAllItems(int userId) {
        return items.values().stream()
                .filter(item ->
                        item.getOwnerId() == userId)
                .collect(Collectors.toList());
    }

    /**
     * Get list of searched items
     *
     * @param request for item
     * @return list of items
     */
    @Override
    public List<Item> searchItems(String request) {
        if (!request.isEmpty()) {
            return items.values().stream()
                    .filter(Item::getIsAvailable)
                    .filter(item -> item.getName().toLowerCase().contains(request.toLowerCase()) ||
                            item.getDescription().toLowerCase().contains(request.toLowerCase()))
                    .collect(Collectors.toList());
        } else
            return new ArrayList<>();
    }
}
