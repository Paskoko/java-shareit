package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

/**
 * Class service for operations with users storage
 */
@Service
public class UserServiceImpl implements UserService {

    @Qualifier("userInMemoryStorage")
    private final UserStorage userStorage;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, UserRepository userRepository) {
        this.userStorage = userStorage;
        this.userRepository = userRepository;
    }

    /**
     * Add new user with validation
     *
     * @param user to add
     * @return added user
     */
    @Override
    public UserDto createUser(UserDto user) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
    }

    /**
     * Update user with validation
     *
     * @param user to update
     * @return updated user
     */
    @Transactional
    @Override
    public UserDto updateUser(UserDto user, int userId) {
        user.setId(userId);
        User userFromRep = userRepository.findById(userId).orElseThrow();
        userFromRep = userFromRep.compare(UserMapper.toUser(user));
        return UserMapper.toUserDto(userRepository.save(userFromRep));
    }

    /**
     * Delete user by id
     *
     * @param id of user
     */
    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    /**
     * Get user by id
     *
     * @param id of user
     * @return user
     */
    @Override
    public UserDto getUserById(int id) {
        return UserMapper.toUserDto(userRepository.findById(id).orElseThrow());
    }

    /**
     * Get list with all users
     *
     * @return list with all users
     */
    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toListUserDto(userRepository.findAll());
    }


}
