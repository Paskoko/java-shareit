package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Map;

import static ru.practicum.shareit.util.Util.checkUserId;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${sv.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }


    /**
     * Handle create request
     *
     * @param bookingPostDto to create
     * @param userId         of creator
     * @return response from server
     */
    public ResponseEntity<Object> createBooking(BookingPostDto bookingPostDto, String userId) {
        checkUserId(userId);
        validateDate(bookingPostDto);
        return post("", Integer.parseInt(userId), bookingPostDto);
    }

    /**
     * Handle update request
     *
     * @param bookingId of booking
     * @param approved  booking status
     *                  true = approved
     *                  false = rejected
     * @param userId    user id
     * @return response from server
     */
    public ResponseEntity<Object> updateBookingStatus(int bookingId, boolean approved, String userId) {
        checkUserId(userId);
        Map<String, Object> parameters = Map.of(
                "approved", approved);
        return patch("/" + bookingId + "?approved={approved}", Integer.parseInt(userId), parameters, null);
    }


    /**
     * Handle get booking by id request
     *
     * @param bookingId of booking
     * @param userId    user id
     * @return response from server
     */
    public ResponseEntity<Object> getBooking(int bookingId, String userId) {
        checkUserId(userId);
        return get("/" + bookingId, Integer.parseInt(userId));
    }


    /**
     * Handle get all bookings request
     *
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param state  of booking
     * @param userId user id
     * @return response from server
     */
    public ResponseEntity<Object> getAllBookings(BookingState state, Integer from, Integer size, String userId) {
        checkUserId(userId);
        if ((from != null) && (size != null) && (state != null)) {
            Map<String, Object> parameters = Map.of(
                    "state", state,
                    "from", from,
                    "size", size
            );
            return get("?state={state}&from={from}&size={size}", Integer.parseInt(userId), parameters);
        }
        if (state != null) {
            Map<String, Object> parameters = Map.of(
                    "state", state);
            return get("?state={state}", Integer.parseInt(userId), parameters);
        }
        return get("", Integer.parseInt(userId));
    }

    /**
     * Handle get all bookings request for all items
     *
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param state  of booking
     * @param userId user id
     * @return response from server
     */
    public ResponseEntity<Object> getAllBookingsForAllItems(BookingState state,
                                                            Integer from, Integer size, String userId) {
        checkUserId(userId);
        if ((from != null) && (size != null) && (state != null)) {
            Map<String, Object> parameters = Map.of(
                    "state", state,
                    "from", from,
                    "size", size
            );
            return get("/owner?state={state}&from={from}&size={size}", Integer.parseInt(userId), parameters);
        }
        if (state != null) {
            Map<String, Object> parameters = Map.of(
                    "state", state);
            return get("/owner?state={state}", Integer.parseInt(userId), parameters);
        }
        return get("/owner", Integer.parseInt(userId));
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
}
