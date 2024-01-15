package ru.practicum.shareit.util.exceptions;

/**
 * Custom exception for resource's conflicts
 */
public class ResourceConflictException extends RuntimeException {
    public ResourceConflictException(String message) {
        super(message);
    }
}
