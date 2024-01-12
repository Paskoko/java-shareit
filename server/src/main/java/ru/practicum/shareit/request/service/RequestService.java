package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

/**
 * Interface for request service
 */
public interface RequestService {

    /**
     * Add new request with validation
     *
     * @param requestDto to add
     * @param userId     of owner
     * @return added request
     */
    RequestDto createRequest(RequestDto requestDto, String userId);


    /**
     * Get list of all user's requests
     *
     * @param userId of owner
     * @return lists of user's requests
     */
    List<RequestDto> getUserRequests(String userId);


    /**
     * Get list of all requests
     *
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param userId of user
     * @return list with requests
     */
    List<RequestDto> getAllRequests(Integer from, Integer size, String userId);


    /**
     * Get request by id
     *
     * @param requestId of request
     * @param userId    of user
     * @return request
     */
    RequestDto getRequestById(int requestId, String userId);
}
