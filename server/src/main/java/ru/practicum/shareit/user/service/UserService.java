package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Interface for user service
 */
public interface UserService {

    /**
     * Add new user with validation
     *
     * @param user to add
     * @return added user
     */
    UserDto createUser(UserDto user);

    /**
     * Update user with validation
     *
     * @param user to update
     * @return updated user
     */
    UserDto updateUser(UserDto user, int userId);

    /**
     * Delete user by id
     *
     * @param id of user
     */
    void deleteUser(int id);

    /**
     * Get user by id
     *
     * @param id of user
     * @return user
     */
    UserDto getUserById(int id);

    /**
     * Get list with all users
     *
     * @return list with all users
     */
    List<UserDto> getAllUsers();
}
