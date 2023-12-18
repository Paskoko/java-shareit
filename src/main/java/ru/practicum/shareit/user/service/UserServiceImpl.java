package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

/**
 * Class service for operations with users storage
 */
@Service
public class UserServiceImpl implements UserService {

    @Qualifier("userInMemoryStorage")
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Add new user with validation
     *
     * @param user to add
     * @return added user
     */
    @Override
    public UserDto createUser(UserDto user) {
        return UserMapper.toUserDto(userStorage.addUser(UserMapper.toUser(user, user.getId())));
    }

    /**
     * Update user with validation
     *
     * @param user to update
     * @return updated user
     */
    @Override
    public UserDto updateUser(UserDto user, int userId) {
        return UserMapper.toUserDto(userStorage.updateUser(UserMapper.toUser(user, userId)));
    }

    /**
     * Delete user by id
     *
     * @param id of user
     */
    @Override
    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    /**
     * Get user by id
     *
     * @param id of user
     * @return user
     */
    @Override
    public UserDto getUserById(int id) {
        return UserMapper.toUserDto(userStorage.getUserById(id));
    }

    /**
     * Get list with all users
     *
     * @return list with all users
     */
    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toListUserDto(userStorage.getAllUsers());
    }
}
