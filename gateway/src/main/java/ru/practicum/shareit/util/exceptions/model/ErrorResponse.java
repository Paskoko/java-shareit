package ru.practicum.shareit.util.exceptions.model;

import lombok.Data;

/**
 * Class for response errors
 */
@Data
public class ErrorResponse {
    private final String error;
}