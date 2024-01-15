package ru.practicum.shareit.util.exceptions;

/**
 * Custom exception for booking's status
 */
public class UnsupportedStatusException extends IllegalArgumentException {
    public UnsupportedStatusException(String message) {
        super(message);
    }
}
