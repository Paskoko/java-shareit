package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.util.BookingStatus;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class Booking {
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
