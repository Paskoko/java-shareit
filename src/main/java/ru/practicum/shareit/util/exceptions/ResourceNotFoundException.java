package ru.practicum.shareit.util.exceptions;

/**
 * Custom exception for resources
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
