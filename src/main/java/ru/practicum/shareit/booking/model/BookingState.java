package ru.practicum.shareit.booking.model;

/**
 * Enum for booking states
 * only for database objects
 */
public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED
}
