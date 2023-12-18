package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

/**
 * Class with user's components
 */
@Data
@Builder
public class User {
    private int id;
    private String name;
    @Email
    private String email;
}
