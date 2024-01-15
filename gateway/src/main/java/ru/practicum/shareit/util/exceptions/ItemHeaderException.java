package ru.practicum.shareit.util.exceptions;

/**
 * Custom exception for items without userId header
 */
public class ItemHeaderException extends RuntimeException {
    public ItemHeaderException(String message) {
        super(message);
    }

}
