package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * DTO class for booking in post requests
 */
@Data
@Builder
public class BookingPostDto {
    private int itemId;
    @DateTimeFormat
    private LocalDateTime start;
    @DateTimeFormat
    private LocalDateTime end;
}
