package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingController bookingController;

    private static final LocalDateTime start = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime end = LocalDateTime.now().plusDays(2);
    User booker;
    User user;
    BookingDto bookingDto;
    BookingDto bookingDto2;
    BookingPostDto bookingPostDto;

    @BeforeEach
    void setUp() {
        Item item = Item.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .isAvailable(true)
                .build();
        Item item2 = Item.builder()
                .id(2)
                .name("test item 2")
                .description("test")
                .ownerId(1)
                .isAvailable(true)
                .build();
        user = User.builder()
                .id(1)
                .name("user")
                .email("user@email.com")
                .build();
        booker = User.builder()
                .id(2)
                .name("booker")
                .email("booker@email.com")
                .build();
        bookingDto = BookingDto.builder()
                .id(1)
                .start(start)
                .end(end)
                .booker(booker)
                .item(item)
                .status(BookingStatusDto.APPROVED)
                .build();
        bookingDto2 = BookingDto.builder()
                .id(2)
                .start(start.plusHours(1))
                .end(end.plusHours(1))
                .booker(booker)
                .item(item2)
                .status(BookingStatusDto.WAITING)
                .build();
        bookingPostDto = BookingPostDto.builder()
                .itemId(1)
                .start(start)
                .end(end)
                .build();
    }

    @Test
    void createBookingTest() throws Exception {
        when(bookingService.createBooking(bookingPostDto, "1"))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(bookingPostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), User.class))
                .andExpect(jsonPath("$.item", is(bookingDto.getItem()), Item.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void updateBookingStatusTest() throws Exception {
        when(bookingService.updateBookingStatus(bookingDto.getId(), true, "1"))
                .thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .contentType("application/json")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), User.class))
                .andExpect(jsonPath("$.item", is(bookingDto.getItem()), Item.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getBookingTest() throws Exception {
        when(bookingService.getBooking(bookingDto.getId(), "1"))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), User.class))
                .andExpect(jsonPath("$.item", is(bookingDto.getItem()), Item.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getAllBookingsTest() throws Exception {
        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);
        bookingDtoList.add(bookingDto2);

        when(bookingService.getAllBookings(BookingState.ALL, 0, 20, "2"))
                .thenReturn(bookingDtoList);

        mockMvc.perform(get("/bookings")
                        .contentType("application/json")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "20")
                        .header("X-Sharer-User-Id", "2"))
                .andExpect(status().isOk());

    }

    @Test
    void getAllBookingsForAllItems() throws Exception {
        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);
        bookingDtoList.add(bookingDto2);

        when(bookingService.getAllBookingsForAllItems(BookingState.ALL, 0, 20, "1"))
                .thenReturn(bookingDtoList);

        mockMvc.perform(get("/bookings/owner")
                        .contentType("application/json")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "20")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }
}