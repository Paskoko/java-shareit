package ru.practicum.shareit.booking.dto;

import java.util.Arrays;
import java.util.Optional;

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
    REJECTED;

    public static Optional<BookingState> from(String stringState) {
        return Arrays.stream(values())
                .filter(state -> state.name().equalsIgnoreCase(stringState))
                .findFirst();
    }
}
