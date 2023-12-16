package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

/**
 * Class mapper for itemRequest object
 */
public class RequestMapper {
    /**
     * Transform itemRequest to itemRequestDto object
     *
     * @param itemRequest to transform
     * @return itemRequestDto object
     */
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestId(itemRequest.getRequestId())
                .time(itemRequest.getTime())
                .build();
    }

    /**
     * Transform itemRequestDto to itemRequest object
     *
     * @param itemRequestDto to transform
     * @return itemRequest object
     */
    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestId(itemRequestDto.getRequestId())
                .time(itemRequestDto.getTime())
                .build();
    }
}
