package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

/**
 * Class controller for users
 */
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;

    /**
     * POST request handler with validation
     *
     * @param user to add
     * @return added user
     */
    @PostMapping()
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto user) {
        return userClient.createUser(user);
    }

    /**
     * GET user by id request handler
     *
     * @param userId of user
     * @return user
     */
    @GetMapping(value = "/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable int userId) {
        return userClient.getUserById(userId);
    }

    /**
     * PUT request handler with validation
     *
     * @param updatedUser to update
     * @param userId      of user
     * @return updated user
     */
    @PatchMapping(value = "/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto updatedUser, @PathVariable Integer userId) {
        return userClient.updateUser(updatedUser, userId);
    }

    /**
     * DELETE request handler
     *
     * @param userId of user to delete
     */
    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer userId) {
        return userClient.deleteUser(userId);
    }

    /**
     * GET request handler
     *
     * @return list of all saved users
     */
    @GetMapping()
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }
}
