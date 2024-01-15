package ru.practicum.shareit.util;

import ru.practicum.shareit.util.exceptions.ItemHeaderException;

/**
 * Util class
 */
public class Util {
    /**
     * Validation of item's owner
     *
     * @param userId of owner
     */
    public static void checkUserId(String userId) {
        if (userId == null) {
            throw new ItemHeaderException("No header with user id!");
        }
    }
}
