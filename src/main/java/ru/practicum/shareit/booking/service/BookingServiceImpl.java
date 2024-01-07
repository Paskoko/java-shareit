package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exceptions.ItemHeaderException;
import ru.practicum.shareit.util.exceptions.ResourceNotFoundException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class service for operations with bookings storage
 */
@Service
public class BookingServiceImpl implements BookingService {

    private final UserService userService;

    private final ItemService itemService;

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(UserService userService, ItemService itemService, BookingRepository bookingRepository) {
        this.userService = userService;
        this.itemService = itemService;
        this.bookingRepository = bookingRepository;
    }


    /**
     * Add new booking
     * with validation of booking's dates
     *
     * @param bookingPostDto to add
     * @param userId         of user
     * @return added booking
     */
    @Override
    public BookingDto createBooking(BookingPostDto bookingPostDto, String userId) {
        validateItem(bookingPostDto.getItemId(), userId);
        validateDate(bookingPostDto);

        return BookingMapper.toBookingDto(bookingRepository.save(toNewBooking(bookingPostDto, userId)));
    }

    /**
     * Update status for booking
     *
     * @param bookingId of booking
     * @param status    of booking
     * @param userId    of user
     * @return updated booking
     */
    @Override
    public BookingDto updateBookingStatus(int bookingId, boolean status, String userId) {
        validateUserId(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();

        if (!booking.getState().equals(BookingState.WAITING)) {
            throw new ValidationException("Owner has already checked this booking!");
        }

        if (Integer.parseInt(userId) != booking.getItem().getOwnerId()) {
            throw new ResourceNotFoundException("Only item's owner can approve!");
        }

        BookingState bookingState = BookingState.CURRENT;
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            bookingState = BookingState.PAST;
        } else if (booking.getStart().isAfter(LocalDateTime.now())) {
            bookingState = BookingState.FUTURE;
        }

        booking.setState(status ? bookingState : BookingState.REJECTED);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    /**
     * Get booking for user
     * with validation of access to booking
     *
     * @param bookingId of booking
     * @param userId    of user
     * @return booking
     */
    @Override
    public BookingDto getBooking(int bookingId, String userId) {
        validateUserId(userId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();

        if ((Integer.parseInt(userId) != booking.getItem().getOwnerId() &&
                (Integer.parseInt(userId) != booking.getBooker().getId()))) {
            throw new ResourceNotFoundException("Wrong booking id for that user!");
        }

        return BookingMapper.toBookingDto(booking);
    }

    /**
     * Get all bookings for user
     *
     * @param bookingState of bookings
     * @param userId       of owner
     * @return sorted list of all bookings
     */
    @Override
    public List<BookingDto> getAllBookings(BookingState bookingState, String userId) {
        validateUserId(userId);

        BooleanExpression byBookerId = QBooking.booking.booker.id.eq(Integer.parseInt(userId));
        BooleanExpression byState = QBooking.booking.state.isNotNull();
        Sort.Direction sort = Sort.Direction.DESC;
        if (!bookingState.equals(BookingState.ALL)) {
            byState = QBooking.booking.state.eq(bookingState);
        }
        LocalDateTime now = LocalDateTime.now();

        if (bookingState.equals(BookingState.FUTURE))           // Check other bookings to be future
            byState = byState.or(QBooking.booking.start.after(now));

        if (bookingState.equals(BookingState.PAST))             // Check other bookings to be past
            byState = byState.or(QBooking.booking.end.before(now));

        if (bookingState.equals(BookingState.CURRENT)) {        // Check other bookings to be current
            byState = byState.or(QBooking.booking.start.before(now)
                    .and(QBooking.booking.end.after(now)));
            sort = Sort.Direction.ASC;
        }


        List<Booking> bookingList = (List<Booking>) bookingRepository.findAll(byBookerId.and(byState),
                Sort.by(sort, "start"));

        return BookingMapper.toListBookingDto(bookingList);
    }

    /**
     * Get list of all booking for all user's items
     *
     * @param bookingState of bookings
     * @param userId       of owner
     * @return list of all bookings
     */
    @Override
    public List<BookingDto> getAllBookingsForAllItems(BookingState bookingState, String userId) {
        validateUserId(userId);

        if (itemService.getAllItems(userId).isEmpty())
            return null;
        else {
            BooleanExpression byItem = QBooking.booking.item.ownerId.eq(Integer.parseInt(userId));
            BooleanExpression byState = QBooking.booking.state.isNotNull();
            Sort.Direction sort = Sort.Direction.DESC;
            if (!bookingState.equals(BookingState.ALL)) {
                byState = QBooking.booking.state.eq(bookingState);
            }
            LocalDateTime now = LocalDateTime.now();

            if (bookingState.equals(BookingState.FUTURE))           // Check other bookings to be future
                byState = byState.or(QBooking.booking.start.after(now));

            if (bookingState.equals(BookingState.PAST))             // Check other bookings to be past
                byState = byState.or(QBooking.booking.end.before(now));

            if (bookingState.equals(BookingState.CURRENT)) {        // Check other bookings to be current
                byState = byState.or(QBooking.booking.start.before(now)
                        .and(QBooking.booking.end.after(now)));
                sort = Sort.Direction.ASC;
            }

            Iterable<Booking> bookingList = bookingRepository.findAll(byItem.and(byState),
                    Sort.by(sort, "start"));

            return BookingMapper.toListBookingDto((List<Booking>) bookingList);
        }
    }

    /**
     * Validation of booking's owner
     *
     * @param userId of owner
     */
    private void validateUserId(String userId) {
        if (userId == null) {
            throw new ItemHeaderException("No header with user id!");
        }

        int id = Integer.parseInt(userId);

        userService.getUserById(id);    // Validation of user id
    }

    /**
     * Validation of item to be available
     *
     * @param itemId of item to validate
     * @param userID to validate item's owner
     */
    private void validateItem(int itemId, String userID) {
        if (!itemService.getItemById(itemId, userID).getAvailable()) {
            throw new ValidationException("Item is not available for booking!");
        }
        if (itemService.getItemById(itemId, userID).getOwnerId() == Integer.parseInt(userID)) {
            throw new ResourceNotFoundException("Owner cannot book it's item!");
        }
    }

    /**
     * Validation of booking's date
     *
     * @param bookingPostDto to validate
     */
    private void validateDate(BookingPostDto bookingPostDto) {
        if ((bookingPostDto.getStart() == null) || (bookingPostDto.getEnd() == null) ||
                (bookingPostDto.getStart().equals(bookingPostDto.getEnd())) ||
                (bookingPostDto.getEnd().isBefore(bookingPostDto.getStart())) ||
                (bookingPostDto.getStart().isBefore(LocalDateTime.now()))) {
            throw new ValidationException("Wrong date!");
        }
    }

    /**
     * Add item and user to booking object
     * while transforming from bookingPostDto to booking
     * if booking is not exist in database
     *
     * @param bookingPostDto to transform
     * @return booking object
     */
    private Booking toNewBooking(BookingPostDto bookingPostDto, String userId) {
        BookingDto bookingDto = BookingMapper.toBookingDto(bookingPostDto);
        bookingDto.setItem(ItemMapper.toItem(itemService.getItemById(bookingPostDto.getItemId(), userId)));
        bookingDto.setBooker(UserMapper.toUser(userService.getUserById(Integer.parseInt(userId))));
        bookingDto.setStatus(BookingStatusDto.WAITING);

        return BookingMapper.toBooking(bookingDto);
    }
}
