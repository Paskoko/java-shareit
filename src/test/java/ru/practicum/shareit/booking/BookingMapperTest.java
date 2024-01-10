package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookingMapperTest {

    public static final LocalDateTime start = LocalDateTime.now().plusDays(1);
    public static final LocalDateTime end = LocalDateTime.now().plusDays(2);
    Item item;
    User booker;
    Booking booking;
    BookingDto bookingDto;
    BookingPostDto bookingPostDto;
    BookingItemDto bookingItemDto;

    @BeforeEach
    void setUp() {
        booker = User.builder()
                .id(1)
                .name("booker")
                .email("booker@email.com")
                .build();

        booking = Booking.builder()
                .id(1)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .state(BookingState.WAITING)
                .build();
        bookingPostDto = BookingPostDto.builder()
                .itemId(1)
                .start(start)
                .end(end)
                .build();
        bookingDto = BookingDto.builder()
                .id(1)
                .start(start)
                .end(end)
                .booker(booker)
                .item(item)
                .status(BookingStatusDto.WAITING)
                .build();
        bookingItemDto = BookingItemDto.builder()
                .id(1)
                .bookerId(1)
                .start(start)
                .end(end)
                .build();
    }

    @Test
    void toBookingDto() {
        BookingDto result = BookingMapper.toBookingDto(booking);

        assertNotNull(result);
        assertEquals(bookingDto.getId(), result.getId());
        assertEquals(bookingDto.getStart(), result.getStart());
        assertEquals(bookingDto.getEnd(), result.getEnd());
        assertEquals(bookingDto.getItem(), result.getItem());
        assertEquals(bookingDto.getBooker(), result.getBooker());
        assertEquals(bookingDto.getStatus(), result.getStatus());
    }

    @Test
    void testToBookingDto() {
        BookingDto result = BookingMapper.toBookingDto(bookingPostDto);

        assertNotNull(result);
        assertEquals(bookingDto.getStart(), result.getStart());
        assertEquals(bookingDto.getEnd(), result.getEnd());
    }

    @Test
    void toBooking() {
        Booking result = BookingMapper.toBooking(bookingDto);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getItem(), result.getItem());
        assertEquals(booking.getBooker(), result.getBooker());
        assertEquals(booking.getState(), result.getState());
    }

    @Test
    void toBookingRejected() {
        booking.setState(BookingState.REJECTED);
        bookingDto.setStatus(BookingStatusDto.REJECTED);
        Booking result = BookingMapper.toBooking(bookingDto);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getItem(), result.getItem());
        assertEquals(booking.getBooker(), result.getBooker());
        assertEquals(booking.getState(), result.getState());
    }

    @Test
    void toBookingPast() {
        booking.setState(BookingState.PAST);
        booking.setStart(start.minusDays(11));
        booking.setEnd(end.minusDays(10));
        bookingDto.setStatus(BookingStatusDto.APPROVED);
        bookingDto.setStart(start.minusDays(11));
        bookingDto.setEnd(end.minusDays(10));
        Booking result = BookingMapper.toBooking(bookingDto);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getItem(), result.getItem());
        assertEquals(booking.getBooker(), result.getBooker());
        assertEquals(booking.getState(), result.getState());
    }

    @Test
    void toBookingFuture() {
        booking.setState(BookingState.FUTURE);
        bookingDto.setStatus(BookingStatusDto.APPROVED);
        Booking result = BookingMapper.toBooking(bookingDto);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getItem(), result.getItem());
        assertEquals(booking.getBooker(), result.getBooker());
        assertEquals(booking.getState(), result.getState());
    }

    @Test
    void toBookingItemDto() {
        BookingItemDto result = BookingMapper.toBookingItemDto(booking);

        assertNotNull(result);
        assertEquals(bookingItemDto.getId(), result.getId());
        assertEquals(bookingItemDto.getBookerId(), result.getBookerId());
        assertEquals(bookingItemDto.getStart(), result.getStart());
        assertEquals(bookingItemDto.getEnd(), result.getEnd());
    }

    @Test
    void toListBookingDto() {
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);

        List<BookingDto> result = BookingMapper.toListBookingDto(bookingList);

        assertNotNull(result);
        assertEquals(bookingDtoList, result);
    }
}