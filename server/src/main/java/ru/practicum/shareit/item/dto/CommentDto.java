package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * DTO class for comment
 */
@Data
@Builder
public class CommentDto {
    private int id;
    @NotBlank
    private String text;
    private String authorName;
    @DateTimeFormat
    private LocalDateTime created;
}
