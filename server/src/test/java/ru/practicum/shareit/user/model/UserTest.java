package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    User comparedUser = User.builder()
            .id(1)
            .name("user")
            .email("user@mail.com")
            .build();

    @Test
    void compareWithNull() {
        User user = User.builder()
                .id(1)
                .name(null)
                .email(null)
                .build();

        User result = comparedUser.compare(user);

        assertEquals(comparedUser, result);
    }

    @Test
    void compareWithFull() {
        User user = User.builder()
                .id(1)
                .name("user")
                .email("user@mail.com")
                .build();

        User result = comparedUser.compare(user);

        assertEquals(comparedUser, result);
    }
}