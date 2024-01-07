package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class mapper for booking object
 */
public class BookingMapper {
    /**
     * Transform booking to bookingDto object
     *
     * @param booking to transform
     * @return bookingDto object
     */
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .status(stateToStatus(booking))
                .build();
    }

    /**
     * Transform bookingPostDto to bookingDto object
     *
     * @param bookingPostDto to transform
     * @return bookingDto object
     */
    public static BookingDto toBookingDto(BookingPostDto bookingPostDto) {
        return BookingDto.builder()
                .start(bookingPostDto.getStart())
                .end(bookingPostDto.getEnd())
                .build();
    }

    /**
     * Transform bookingDto to booking object
     *
     * @param bookingDto to transform
     * @return booking object
     */
    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(bookingDto.getItem())
                .booker(bookingDto.getBooker())
                .state(statusToState(bookingDto))
                .build();
    }

    /**
     * Transform booking to bookingItemDto object
     *
     * @param booking to transform
     * @return bookingItemDto object
     */
    public static BookingItemDto toBookingItemDto(Booking booking) {
        return BookingItemDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

    /**
     * Transform list of booking to bookingDto objects
     *
     * @param bookingList to transform
     * @return list of bookingDto
     */
    public static List<BookingDto> toListBookingDto(List<Booking> bookingList) {
        return bookingList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    /**
     * Transform status to state
     * DTO object only with status
     * Database objects only with state
     *
     * @param bookingDto object to transform status
     * @return state for booking object
     */
    private static BookingState statusToState(BookingDto bookingDto) {
        BookingState bookingState = BookingState.CURRENT;

        switch (bookingDto.getStatus()) {
            case WAITING:
                bookingState = BookingState.WAITING;
                break;
            case REJECTED:
                bookingState = BookingState.REJECTED;
                break;
            case APPROVED:
                if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
                    bookingState = BookingState.PAST;
                } else if (bookingDto.getStart().isAfter(LocalDateTime.now())) {
                    bookingState = BookingState.FUTURE;
                }
                break;
        }

        return bookingState;
    }

    /**
     * Transform state to status
     * DTO object only with status
     * Database objects only with state
     *
     * @param booking to transform
     * @return status
     */
    private static BookingStatusDto stateToStatus(Booking booking) {
        switch (booking.getState()) {
            case CURRENT:
            case PAST:
            case FUTURE:
                return BookingStatusDto.APPROVED;
            case REJECTED:
                return BookingStatusDto.REJECTED;
            case WAITING:
            default:
                return BookingStatusDto.WAITING;
        }
    }
}
