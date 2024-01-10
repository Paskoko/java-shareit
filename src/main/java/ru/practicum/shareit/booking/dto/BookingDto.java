package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * DTO class with booking's components
 */
@Data
@Builder
public class BookingDto {
    private int id;
    @DateTimeFormat
    private LocalDateTime start;
    @DateTimeFormat
    private LocalDateTime end;
    private User booker;
    private Item item;
    private BookingStatusDto status;
}
