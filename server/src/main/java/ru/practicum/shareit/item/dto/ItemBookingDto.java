package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.util.List;

/**
 * DTO class for item get requests to show last and next bookings
 */
@Data
@Builder
public class ItemBookingDto {
    private int id;
    private String name;
    private String description;
    private int ownerId;
    private Boolean available;
    private int requestId;
    private List<CommentDto> comments;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
}
