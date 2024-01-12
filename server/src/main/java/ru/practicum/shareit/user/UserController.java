package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * Class controller for users
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST request handler
     *
     * @param user to add
     * @return added user
     */
    @PostMapping()
    public UserDto createUser(@RequestBody UserDto user) {
        return userService.createUser(user);
    }

    /**
     * GET user by id request handler
     *
     * @param userId of user
     * @return user
     */
    @GetMapping(value = "/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        return userService.getUserById(userId);
    }

    /**
     * PUT request handler
     *
     * @param updatedUser to update
     * @param userId      of user
     * @return updated user
     */
    @PatchMapping(value = "/{userId}")
    public UserDto updateUser(@RequestBody UserDto updatedUser, @PathVariable int userId) {
        return userService.updateUser(updatedUser, userId);
    }

    /**
     * DELETE request handler
     *
     * @param userId of user to delete
     */
    @DeleteMapping(value = "/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

    /**
     * GET request handler
     *
     * @return list of all saved users
     */
    @GetMapping()
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
