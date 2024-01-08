package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO class with request's fields
 */
@Data
@Builder
public class RequestDto {
    private int id;
    @NotBlank
    private String description;
    private int requester;
    @DateTimeFormat
    private LocalDateTime created;
    private List<ItemRequestDto> items;
}