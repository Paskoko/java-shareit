package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * DTO class for booking in item get request
 */
@Data
@Builder
public class BookingItemDto {
    private int id;
    private int bookerId;
    @DateTimeFormat
    private LocalDateTime start;
    @DateTimeFormat
    private LocalDateTime end;
}
