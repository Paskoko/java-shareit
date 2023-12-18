package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Interface for users storage
 */
public interface UserStorage {

    /**
     * Add user to the storage
     *
     * @param user to add
     * @return added user
     */
    User addUser(User user);

    /**
     * Update user
     *
     * @param user to update
     * @return updated user
     */
    User updateUser(User user);

    /**
     * Delete user by id
     *
     * @param id of user
     */
    void deleteUser(int id);

    /**
     * Return user by id
     *
     * @param id of user
     * @return user
     */
    User getUserById(int id);

    /**
     * Return list with all users
     *
     * @return list of all users
     */
    List<User> getAllUsers();

}
