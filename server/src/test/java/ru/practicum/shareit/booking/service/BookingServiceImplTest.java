package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exceptions.ResourceNotFoundException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    UserService mockUserService;

    @Mock
    ItemService mockItemService;

    @Mock
    BookingRepository mockBookingRepository;

    @InjectMocks
    BookingServiceImpl bookingService;

    private static final LocalDateTime start = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime end = LocalDateTime.now().plusDays(2);
    ItemBookingDto itemBookingDto;
    Item item;
    Item item2;
    User booker;
    User user;
    UserDto userDto;
    Booking booking;
    Booking booking2;
    BookingDto bookingDto;
    BookingDto bookingDto2;
    BookingPostDto bookingPostDto;

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
                .ownerId(2)
                .isAvailable(true)
                .build();
        itemBookingDto = ItemBookingDto.builder()
                .id(1)
                .name("test item")
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
                .email("user@emai.com")
                .build();

        booker = User.builder()
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
                .status(BookingStatusDto.APPROVED)
                .build();
    }


    @Test
    void createBooking() {
        String userId = String.valueOf(booker.getId());


        when(mockItemService.getItemById(bookingPostDto.getItemId(), userId))
                .thenReturn(itemBookingDto);
        when(mockUserService.getUserById(Integer.parseInt(userId)))
                .thenReturn(userDto);
        when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);
        BookingDto result = bookingService.createBooking(bookingPostDto, userId);

        assertNotNull(result);
        assertEquals(bookingPostDto.getItemId(), result.getItem().getId());
        assertEquals(bookingPostDto.getStart(), result.getStart());
        assertEquals(bookingPostDto.getEnd(), result.getEnd());
    }

    @Test
    void createBookingWithOwner() {
        String userId = String.valueOf(user.getId());


        when(mockItemService.getItemById(bookingPostDto.getItemId(), userId))
                .thenReturn(itemBookingDto);

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                bookingService.createBooking(bookingPostDto, userId));
    }

    @Test
    void createBookingWithNotAvailable() {
        String userId = String.valueOf(booker.getId());

        itemBookingDto.setAvailable(false);

        when(mockItemService.getItemById(bookingPostDto.getItemId(), userId))
                .thenReturn(itemBookingDto);

        Assertions.assertThrows(ValidationException.class, () ->
                bookingService.createBooking(bookingPostDto, userId));
    }


    @Test
    void updateBookingStatusChecked() {
        String userId = String.valueOf(user.getId());
        int bookingId = booking.getId();
        booking.setState(BookingState.CURRENT);
        boolean approved = true;


        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        Assertions.assertThrows(ValidationException.class, () ->
                bookingService.updateBookingStatus(bookingId, approved, userId));
    }

    @Test
    void updateBookingStatusNotOwner() {
        String userId = String.valueOf(booker.getId());
        int bookingId = booking.getId();
        boolean approved = true;


        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                bookingService.updateBookingStatus(bookingId, approved, userId));
    }


    @Test
    void updateBookingStatusPast() {
        String userId = String.valueOf(user.getId());
        int bookingId = booking.getId();
        booking.setStart(start.minusDays(4));
        booking.setEnd(end.minusDays(3));
        boolean approved = true;

        Booking savedBooking = Booking.builder()
                .id(1)
                .start(start.minusDays(4))
                .end(end.minusDays(3))
                .item(item)
                .booker(booker)
                .state(BookingState.PAST)
                .build();

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(savedBooking);
        BookingDto result = bookingService.updateBookingStatus(bookingId, approved, userId);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(BookingStatusDto.APPROVED, result.getStatus());
    }

    @Test
    void updateBookingStatusFuture() {
        String userId = String.valueOf(user.getId());
        int bookingId = booking.getId();
        boolean approved = true;

        Booking savedBooking = Booking.builder()
                .id(1)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .state(BookingState.FUTURE)
                .build();

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(savedBooking);
        BookingDto result = bookingService.updateBookingStatus(bookingId, approved, userId);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(BookingStatusDto.APPROVED, result.getStatus());
    }

    @Test
    void updateBookingStatusCurrent() {
        String userId = String.valueOf(user.getId());
        int bookingId = booking.getId();
        booking.setStart(start.minusDays(1));
        boolean approved = true;

        Booking savedBooking = Booking.builder()
                .id(1)
                .start(start.minusDays(1))
                .end(end)
                .item(item)
                .booker(booker)
                .state(BookingState.CURRENT)
                .build();

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(savedBooking);
        BookingDto result = bookingService.updateBookingStatus(bookingId, approved, userId);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(BookingStatusDto.APPROVED, result.getStatus());
    }

    @Test
    void updateBookingStatusRejected() {
        String userId = String.valueOf(user.getId());
        int bookingId = booking.getId();
        boolean approved = false;

        Booking savedBooking = Booking.builder()
                .id(1)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .state(BookingState.REJECTED)
                .build();

        when(mockBookingRepository.findById(bookingId))
                .thenReturn(Optional.of(booking));
        when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(savedBooking);
        BookingDto result = bookingService.updateBookingStatus(bookingId, approved, userId);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(BookingStatusDto.REJECTED, result.getStatus());
    }


    @Test
    void getBooking() {
        String userId = String.valueOf(user.getId());

        when(mockBookingRepository.findById(booking.getId()))
                .thenReturn(Optional.of(booking));

        BookingDto result = bookingService.getBooking(booking.getId(), userId);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
    }


    @Test
    void getBookingByBooker() {
        String userId = String.valueOf(booker.getId());

        when(mockBookingRepository.findById(booking.getId()))
                .thenReturn(Optional.of(booking));

        BookingDto result = bookingService.getBooking(booking.getId(), userId);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getBookingWrongUser() {
        String userId = String.valueOf(5);

        when(mockBookingRepository.findById(booking.getId()))
                .thenReturn(Optional.of(booking));

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                bookingService.getBooking(booking.getId(), userId));
    }

    @Test
    void getAllBookingsWithPagination() {
        String userId = String.valueOf(booker.getId());

        int from = 0;
        int size = 20;
        List<Booking> bookingList = new ArrayList<>();
        booking.setState(BookingState.REJECTED);
        bookingList.add(booking);
        booking2.setState(BookingState.REJECTED);
        bookingList.add(booking2);
        Page<Booking> bookingPage = new PageImpl<>(bookingList);

        BooleanExpression byBookerId = QBooking.booking.booker.id.eq(Integer.parseInt(userId));
        BooleanExpression byState = QBooking.booking.state.eq(BookingState.REJECTED);

        Sort.Direction sort = Sort.Direction.DESC;
        PageRequest page = PageRequest.of(from / size, size, Sort.by(sort, "start"));

        when(mockBookingRepository.findAll(byBookerId.and(byState), page))
                .thenReturn(bookingPage);

        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);
        bookingDtoList.add(bookingDto2);


        List<BookingDto> result = bookingService.getAllBookings(BookingState.REJECTED, from, size, userId);

        assertNotNull(result);
        assertEquals(bookingDtoList.size(), result.size());
    }

    @Test
    void getAllBookingsWithoutSize() {
        String userId = String.valueOf(booker.getId());

        List<Booking> bookingList = new ArrayList<>();
        booking.setState(BookingState.FUTURE);
        bookingList.add(booking);
        booking2.setState(BookingState.WAITING);
        bookingList.add(booking2);

        BooleanExpression byBookerId = QBooking.booking.booker.id.eq(Integer.parseInt(userId));
        BooleanExpression byState = QBooking.booking.state.isNotNull();
        Sort.Direction sort = Sort.Direction.DESC;

        when(mockBookingRepository.findAll(byBookerId.and(byState), Sort.by(sort, "start")))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);
        bookingDtoList.add(bookingDto2);

        List<BookingDto> result = bookingService.getAllBookings(BookingState.ALL, 0, null, userId);

        assertNotNull(result);
        assertEquals(bookingDtoList.size(), result.size());
    }

    @Test
    void getAllBookingsWithoutPagination() {
        String userId = String.valueOf(booker.getId());

        List<Booking> bookingList = new ArrayList<>();
        booking.setState(BookingState.FUTURE);
        bookingList.add(booking);
        booking2.setState(BookingState.WAITING);
        bookingList.add(booking2);

        BooleanExpression byBookerId = QBooking.booking.booker.id.eq(Integer.parseInt(userId));
        BooleanExpression byState = QBooking.booking.state.isNotNull();
        Sort.Direction sort = Sort.Direction.DESC;

        when(mockBookingRepository.findAll(byBookerId.and(byState), Sort.by(sort, "start")))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);
        bookingDtoList.add(bookingDto2);

        List<BookingDto> result = bookingService.getAllBookings(BookingState.ALL, null, null, userId);

        assertNotNull(result);
        assertEquals(bookingDtoList.size(), result.size());
    }

    @Test
    void getAllBookingsForAllItemsWithNoItems() {
        String userId = String.valueOf(booker.getId());

        when(mockItemService.getAllItems(null, null, userId))
                .thenReturn(new ArrayList<>());

        assertNull(bookingService.getAllBookingsForAllItems(BookingState.ALL, null, null, userId));
    }

    @Test
    void getAllBookingsForAllItemsWithoutSize() {
        String userId = String.valueOf(booker.getId());

        List<ItemBookingDto> itemBookingDtoList = new ArrayList<>();
        itemBookingDtoList.add(itemBookingDto);


        List<Booking> bookingList = new ArrayList<>();
        booking.setState(BookingState.FUTURE);
        bookingList.add(booking);
        booking2.setState(BookingState.WAITING);
        bookingList.add(booking2);

        BooleanExpression byItem = QBooking.booking.item.ownerId.eq(Integer.parseInt(userId));
        BooleanExpression byState = QBooking.booking.state.isNotNull();
        Sort.Direction sort = Sort.Direction.DESC;

        when(mockItemService.getAllItems(null, null, userId))
                .thenReturn(itemBookingDtoList);
        when(mockBookingRepository.findAll(byItem.and(byState), Sort.by(sort, "start")))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);
        bookingDtoList.add(bookingDto2);

        List<BookingDto> result = bookingService.getAllBookingsForAllItems(BookingState.ALL, 0, null, userId);

        assertNotNull(result);
        assertEquals(bookingDtoList.size(), result.size());
    }

    @Test
    void getAllBookingsForAllItems() {
        String userId = String.valueOf(booker.getId());

        List<ItemBookingDto> itemBookingDtoList = new ArrayList<>();
        itemBookingDtoList.add(itemBookingDto);

        List<Booking> bookingList = new ArrayList<>();
        booking.setState(BookingState.FUTURE);
        bookingList.add(booking);
        booking2.setState(BookingState.WAITING);
        bookingList.add(booking2);

        BooleanExpression byItem = QBooking.booking.item.ownerId.eq(Integer.parseInt(userId));
        BooleanExpression byState = QBooking.booking.state.isNotNull();
        Sort.Direction sort = Sort.Direction.DESC;

        when(mockItemService.getAllItems(null, null, userId))
                .thenReturn(itemBookingDtoList);
        when(mockBookingRepository.findAll(byItem.and(byState), Sort.by(sort, "start")))
                .thenReturn(bookingList);

        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);
        bookingDtoList.add(bookingDto2);

        List<BookingDto> result = bookingService.getAllBookingsForAllItems(BookingState.ALL, null, null, userId);

        assertNotNull(result);
        assertEquals(bookingDtoList.size(), result.size());
    }

    @Test
    void getAllBookingsForAllItemsWithPagination() {
        String userId = String.valueOf(booker.getId());

        List<ItemBookingDto> itemBookingDtoList = new ArrayList<>();
        itemBookingDtoList.add(itemBookingDto);

        int from = 0;
        int size = 20;
        List<Booking> bookingList = new ArrayList<>();
        booking.setState(BookingState.REJECTED);
        bookingList.add(booking);
        booking2.setState(BookingState.REJECTED);
        bookingList.add(booking2);
        Page<Booking> bookingPage = new PageImpl<>(bookingList);

        BooleanExpression byItem = QBooking.booking.item.ownerId.eq(Integer.parseInt(userId));
        BooleanExpression byState = QBooking.booking.state.eq(BookingState.REJECTED);
        Sort.Direction sort = Sort.Direction.DESC;
        PageRequest page = PageRequest.of(from / size, size, Sort.by(sort, "start"));

        when(mockItemService.getAllItems(null, null, userId))
                .thenReturn(itemBookingDtoList);
        when(mockBookingRepository.findAll(byItem.and(byState), page))
                .thenReturn(bookingPage);

        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);
        bookingDtoList.add(bookingDto2);


        List<BookingDto> result = bookingService.getAllBookingsForAllItems(BookingState.REJECTED, from, size, userId);

        assertNotNull(result);
        assertEquals(bookingDtoList.size(), result.size());
    }
}