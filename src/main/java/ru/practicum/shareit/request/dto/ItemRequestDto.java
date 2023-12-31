package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * DTO class with item request's fields
 */
@Data
@Builder
public class ItemRequestDto {
    private int id;
    @NotBlank
    private String description;
    @NotBlank
    private int requestId;
    @DateTimeFormat
    @NotBlank
    private Date time;
}