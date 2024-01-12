package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Class controller for request
 */
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestClient requestClient;


    /**
     * POST request handler with validation
     *
     * @param requestDto to add
     * @param userId     user id
     * @return added request
     */
    @PostMapping()
    public ResponseEntity<Object> createRequest(@Valid @RequestBody RequestDto requestDto,
                                                @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return requestClient.createRequest(requestDto, userId);
    }

    /**
     * GET list of all requests for the user
     *
     * @param userId user id
     * @return list of user's requests
     */
    @GetMapping()
    public ResponseEntity<Object> getUserRequests(@RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return requestClient.getUserRequests(userId);
    }

    /**
     * GET request by its id
     *
     * @param requestId of the request
     * @param userId    user id
     * @return request
     */
    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable int requestId,
                                                 @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return requestClient.getRequestById(requestId, userId);
    }

    /**
     * GET all requests with pagination
     *
     * @param from   index of the first element
     * @param size   of the page
     * @param userId user id
     * @return list of all requests
     */
    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAllRequests(
            @PositiveOrZero @RequestParam(value = "from", required = false) Integer from,
            @Positive @RequestParam(value = "size", required = false) Integer size,
            @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return requestClient.getAllRequests(from, size, userId);
    }

}
