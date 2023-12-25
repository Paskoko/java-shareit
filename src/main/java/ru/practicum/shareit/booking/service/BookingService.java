package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

/**
 * Interface for booking service
 */
public interface BookingService {

    /**
     * Add new booking
     *
     * @param bookingPostDto to add
     * @param userId         of user
     * @return added booking
     */
    BookingDto createBooking(BookingPostDto bookingPostDto, String userId);

    /**
     * Update status for booking
     *
     * @param bookingId of booking
     * @param status    of booking
     * @param userId    of user
     * @return updated booking
     */
    BookingDto updateBookingStatus(int bookingId, boolean status, String userId);

    /**
     * Get booking for user
     *
     * @param bookingId of booking
     * @param userId    of user
     * @return booking
     */
    BookingDto getBooking(int bookingId, String userId);

    /**
     * Get all bookings for user
     *
     * @param bookingState of bookings
     * @param userId       of owner
     * @return sorted list of all bookings
     */
    List<BookingDto> getAllBookings(BookingState bookingState, String userId);

    /**
     * Get list of all booking for all user's items
     *
     * @param bookingState of bookings
     * @param userId       of owner
     * @return list of all bookings
     */
    List<BookingDto> getAllBookingsForAllItems(BookingState bookingState, String userId);
}