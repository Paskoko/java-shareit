package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class mapper for itemRequest object
 */
public class RequestMapper {
    /**
     * Transform request to requestDto object
     *
     * @param request to transform
     * @return requestDto object
     */
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requester(request.getRequester())
                .created(request.getCreated())
                .build();
    }

    /**
     * Transform requestDto to request object
     *
     * @param requestDto to transform
     * @return request object
     */
    public static Request toRequest(RequestDto requestDto) {
        return Request.builder()
                .id(requestDto.getId())
                .description(requestDto.getDescription())
                .requester(requestDto.getRequester())
                .created(requestDto.getCreated())
                .build();
    }


    /**
     * Transform list of request to list of requestDto objects
     *
     * @param requestList to transform
     * @return list of requestDto objects
     */
    public static List<RequestDto> toListRequestDto(List<Request> requestList) {
        return requestList.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }
}
