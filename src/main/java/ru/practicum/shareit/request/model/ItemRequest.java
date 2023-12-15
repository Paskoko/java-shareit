package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Class with item request's fields
 */
@Data
@Builder
public class ItemRequest {
    private int id;
    @NotBlank
    private String description;
    @NotBlank
    private int requestId;
    @DateTimeFormat
    @NotBlank
    private Date time;
}
