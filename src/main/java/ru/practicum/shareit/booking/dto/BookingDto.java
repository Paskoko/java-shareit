package ru.practicum.shareit.booking.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.util.BookingStatus;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * DTO class with booking's components
 */
@Data
@Builder
public class BookingDto {
    private final int id;
    @DateTimeFormat
    @NotBlank
    private Date start;
    @DateTimeFormat
    @NotBlank
    private Date end;
    @NotBlank
    private int itemId;
    @NotBlank
    private int bookerId;
    @NotBlank
    private BookingStatus status;
}
