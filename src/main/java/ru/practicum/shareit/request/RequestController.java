package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * Class controller for request
 */
@RestController
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService requestService;


    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }


    /**
     * POST request handler with validation
     *
     * @param requestDto to add
     * @param userId     user id
     * @return added request
     */
    @PostMapping()
    public RequestDto createRequest(@Valid @RequestBody RequestDto requestDto,
                                    @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return requestService.createRequest(requestDto, userId);
    }

    /**
     * GET list of all requests for the user
     *
     * @param userId user id
     * @return list of user's requests
     */
    @GetMapping()
    public List<RequestDto> getUserRequests(@RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return requestService.getUserRequests(userId);
    }

    /**
     * GET request by its id
     *
     * @param requestId of the request
     * @param userId    user id
     * @return request
     */
    @GetMapping(value = "/{requestId}")
    public RequestDto getRequestById(@PathVariable int requestId,
                                     @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return requestService.getRequestById(requestId, userId);
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
    public List<RequestDto> getAllRequests(
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return requestService.getAllRequests(from, size, userId);
    }

}
