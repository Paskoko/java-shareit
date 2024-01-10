package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.exceptions.UnsupportedStatusException;

import javax.validation.Valid;
import java.util.List;

/**
 * Class controller for booking
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    /**
     * POST request handler with validation
     *
     * @param bookingPostDto to add
     * @param userId         user id
     * @return added booking
     */
    @PostMapping()
    public BookingDto createBooking(@Valid @RequestBody BookingPostDto bookingPostDto,
                                    @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return bookingService.createBooking(bookingPostDto, userId);
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
    public BookingDto updateBookingStatus(@PathVariable int bookingId,
                                          @RequestParam(value = "approved") boolean approved,
                                          @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return bookingService.updateBookingStatus(bookingId, approved, userId);
    }

    /**
     * GET booking by id
     *
     * @param bookingId booking id
     * @param userId    user id
     * @return booking
     */
    @GetMapping(value = "/{bookingId}")
    public BookingDto getBooking(@PathVariable int bookingId,
                                 @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return bookingService.getBooking(bookingId, userId);
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
    public List<BookingDto> getAllBookings(
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
            @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        try {
            BookingState bookingState = BookingState.valueOf(state);
            return bookingService.getAllBookings(bookingState, from, size, userId);
        } catch (IllegalArgumentException exception) {
            throw new UnsupportedStatusException("Unknown state: " + state);
        }
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
    public List<BookingDto> getAllBookingsForAllItems(
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
            @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        try {
            BookingState bookingState = BookingState.valueOf(state);
            return bookingService.getAllBookingsForAllItems(bookingState, from, size, userId);
        } catch (IllegalArgumentException exception) {
            throw new UnsupportedStatusException("Unknown state: " + state);
        }
    }
}
