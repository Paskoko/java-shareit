package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.util.exceptions.UnsupportedStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Class controller for booking
 */
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;


    /**
     * POST request handler with validation
     *
     * @param bookingPostDto to add
     * @param userId         user id
     * @return added booking
     */
    @PostMapping()
    public ResponseEntity<Object> createBooking(@Valid @RequestBody BookingPostDto bookingPostDto,
                                                @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return bookingClient.createBooking(bookingPostDto, userId);
    }

    /**
     * PUT request handler to update booking status
     * with validation
     *
     * @param bookingId of booking
     * @param approved  booking status
     *                  true = approved
     *                  false = rejected
     * @param userId    user id
     * @return updated booking
     */
    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@PathVariable int bookingId,
                                                      @RequestParam(value = "approved") boolean approved,
                                                      @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return bookingClient.updateBookingStatus(bookingId, approved, userId);
    }

    /**
     * GET booking by id
     *
     * @param bookingId booking id
     * @param userId    user id
     * @return booking
     */
    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable int bookingId,
                                             @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return bookingClient.getBooking(bookingId, userId);
    }

    /**
     * GET all bookings for user by state
     *
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param state  of booking
     * @param userId user id
     * @return list of bookings
     */
    @GetMapping()
    public ResponseEntity<Object> getAllBookings(
            @PositiveOrZero @RequestParam(value = "from", required = false) Integer from,
            @Positive @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
            @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + state));
        return bookingClient.getAllBookings(bookingState, from, size, userId);
    }

    /**
     * GET all bookings for all items
     * by state
     *
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param state  of booking
     * @param userId user id
     * @return list of all bookings for all items
     */
    @GetMapping(value = "/owner")
    public ResponseEntity<Object> getAllBookingsForAllItems(
            @PositiveOrZero @RequestParam(value = "from", required = false) Integer from,
            @Positive @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
            @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + state));
        return bookingClient.getAllBookingsForAllItems(bookingState, from, size, userId);
    }
}
