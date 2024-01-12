package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

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
    private UserDto booker;
    private ItemDto item;
    private BookingStatusDto status;
}
