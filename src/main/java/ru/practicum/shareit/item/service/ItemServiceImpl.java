package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exceptions.ItemHeaderException;

import java.util.List;


/**
 * Class service for operations with items storage
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Qualifier("itemInMemoryStorage")
    private final ItemStorage itemStorage;

    private final UserService userService;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }


    /**
     * Add new item with validation
     *
     * @param itemDto to add
     * @param userId  of owner
     * @return added item
     */
    @Override
    public ItemDto createItem(ItemDto itemDto, String userId) {
        validateUserId(userId);
        itemDto.setOwnerId(Integer.parseInt(userId));
        return ItemMapper.toItemDto(itemStorage.addItem(ItemMapper.toItem(itemDto)));
    }

    /**
     * Get item by id with validation
     *
     * @param itemId of item
     * @param userId of owner
     * @return item
     */
    @Override
    public ItemDto getItemById(int itemId, String userId) {
        validateUserId(userId);
        return ItemMapper.toItemDto(itemStorage.getItemById(itemId));
    }

    /**
     * Get list of all user's items
     *
     * @param userId of owner
     * @return list of user's items
     */
    @Override
    public List<ItemDto> getAllItems(String userId) {
        validateUserId(userId);
        return ItemMapper.toListUserDto(itemStorage.getAllItems(Integer.parseInt(userId)));
    }

    /**
     * Update item with validation
     *
     * @param itemDto item to update
     * @param itemId  of item
     * @param userId  of owner
     * @return updated item
     */
    @Override
    public ItemDto updateItem(ItemDto itemDto, int itemId, String userId) {
        validateUserId(userId);
        itemDto.setId(itemId);
        itemDto.setOwnerId(Integer.parseInt(userId));
        return ItemMapper.toItemDto(itemStorage.updateItem(ItemMapper.toItem(itemDto)));
    }

    /**
     * Search for items
     *
     * @param text   to search
     * @param userId of user
     * @return list of found items
     */
    @Override
    public List<ItemDto> searchItems(String text, String userId) {
        validateUserId(userId);
        return ItemMapper.toListUserDto(itemStorage.searchItems(text));

    }

    /**
     * Validation of item's owner
     *
     * @param userId of owner
     */
    private void validateUserId(String userId) {
        if (userId != null) {
            int id = Integer.parseInt(userId);
            userService.getUserById(id);    // Validation of user id
        } else {
            throw new ItemHeaderException("No header with user id!");
        }
    }
}
