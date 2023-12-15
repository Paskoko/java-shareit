package ru.practicum.shareit.item.model;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Class with feedback's components
 */
@Data
@Builder
public class Feedback {
    @NotBlank
    private int userId;
    private String comment;
}
