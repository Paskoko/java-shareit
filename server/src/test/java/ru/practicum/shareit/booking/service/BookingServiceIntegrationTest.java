package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceIntegrationTest {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private static final LocalDateTime start = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime end = LocalDateTime.now().plusDays(2);
    Item item;
    ItemDto itemDto;
    Item item2;
    ItemDto itemDto2;
    User user;
    UserDto userDto;
    User booker;
    UserDto bookerDto;
    Booking booking;
    Booking booking2;
    BookingDto bookingDto;
    BookingDto bookingDto2;
    BookingPostDto bookingPostDto;
    BookingPostDto bookingPostDto2;


    UserDto createdUser;
    UserDto createdBooker;
    ItemDto createdItem;


    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .isAvailable(true)
                .build();
        item2 = Item.builder()
                .id(2)
                .name("test item 2")
                .description("test")
                .ownerId(1)
                .isAvailable(true)
                .build();
        itemDto = ItemDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .available(true)
                .build();
        itemDto2 = ItemDto.builder()
                .id(2)
                .name("test item 2")
                .description("test")
                .ownerId(1)
                .available(true)
                .build();

        user = User.builder()
                .id(1)
                .name("user")
                .email("user@email.com")
                .build();
        userDto = UserDto.builder()
                .id(1)
                .name("user")
                .email("user@email.com")
                .build();
        booker = User.builder()
                .id(2)
                .name("booker")
                .email("booker@email.com")
                .build();
        bookerDto = UserDto.builder()
                .id(2)
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
        booking2 = Booking.builder()
                .id(2)
                .start(start.plusHours(1))
                .end(end.plusHours(1))
                .booker(booker)
                .item(item2)
                .state(BookingState.WAITING)
                .build();
        bookingPostDto = BookingPostDto.builder()
                .itemId(1)
                .start(start)
                .end(end)
                .build();
        bookingPostDto2 = BookingPostDto.builder()
                .itemId(2)
                .start(start.plusHours(1))
                .end(end.plusHours(1))
                .build();
        bookingDto = BookingDto.builder()
                .id(1)
                .start(start)
                .end(end)
                .booker(booker)
                .item(item)
                .status(BookingStatusDto.WAITING)
                .build();
        bookingDto2 = BookingDto.builder()
                .id(2)
                .start(start.plusHours(1))
                .end(end.plusHours(1))
                .booker(booker)
                .item(item2)
                .status(BookingStatusDto.WAITING)
                .build();

        createdUser = userService.createUser(userDto);
        createdBooker = userService.createUser(bookerDto);
        itemDto.setOwnerId(createdUser.getId());
        createdItem = itemService.createItem(itemDto, String.valueOf(createdUser.getId()));

        bookingDto.setBooker(UserMapper.toUser(createdBooker));
        bookingDto.setItem(ItemMapper.toItem(createdItem));
        bookingPostDto.setItemId(createdItem.getId());
    }


    @Test
    void createBookingTest() {
        String userId = String.valueOf(createdBooker.getId());

        BookingDto result = bookingService.createBooking(bookingPostDto, userId);
        bookingDto.setId(result.getId());

        assertThat(result).isEqualTo(bookingDto);
    }

    @Test
    void updateBookingStatusTest() {
        String userId = String.valueOf(createdBooker.getId());

        BookingDto createdBooking = bookingService.createBooking(bookingPostDto, userId);
        userId = String.valueOf(createdUser.getId());
        BookingDto result = bookingService.updateBookingStatus(createdBooking.getId(), false, userId);
        bookingDto.setId(result.getId());
        bookingDto.setStatus(BookingStatusDto.REJECTED);

        assertThat(result).isEqualTo(bookingDto);
    }

    @Test
    void getBookingTest() {
        String userId = String.valueOf(createdBooker.getId());

        BookingDto createdBooking = bookingService.createBooking(bookingPostDto, userId);
        BookingDto result = bookingService.getBooking(createdBooking.getId(), userId);
        bookingDto.setId(result.getId());

        assertThat(result).isEqualTo(bookingDto);
    }

    @Test
    void getAllBookingsTest() {
        String userId = String.valueOf(createdUser.getId());
        itemDto2.setOwnerId(createdUser.getId());

        ItemDto createdItem2 = itemService.createItem(itemDto2, userId);

        bookingDto2.setBooker(UserMapper.toUser(createdBooker));
        bookingDto2.setItem(ItemMapper.toItem(createdItem2));
        bookingPostDto2.setItemId(createdItem2.getId());

        userId = String.valueOf(createdBooker.getId());

        bookingService.createBooking(bookingPostDto, userId);
        bookingService.createBooking(bookingPostDto2, userId);

        List<BookingDto> result = bookingService.getAllBookings(BookingState.ALL, null, null, userId);

        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto2);
        bookingDtoList.add(bookingDto);

        assertThat(result.size()).isEqualTo(bookingDtoList.size());
    }

    @Test
    void getAllBookingsForAllItemsTest() {
        String userId = String.valueOf(createdUser.getId());

        ItemDto createdItem2 = itemService.createItem(itemDto2, userId);

        bookingDto2.setBooker(UserMapper.toUser(createdBooker));
        bookingDto2.setItem(ItemMapper.toItem(createdItem2));
        bookingPostDto2.setItemId(createdItem2.getId());

        userId = String.valueOf(createdBooker.getId());

        bookingService.createBooking(bookingPostDto, userId);
        bookingService.createBooking(bookingPostDto2, userId);

        userId = String.valueOf(createdUser.getId());
        List<BookingDto> result = bookingService.getAllBookingsForAllItems(BookingState.ALL,
                null, null, userId);

        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto2);
        bookingDtoList.add(bookingDto);

        assertThat(result.size()).isEqualTo(bookingDtoList.size());
    }
}
