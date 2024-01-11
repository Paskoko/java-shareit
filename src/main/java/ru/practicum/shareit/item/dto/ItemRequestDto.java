package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO class for item to show with requests
 */
@Data
@Builder
public class ItemRequestDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private int requestId;
}
