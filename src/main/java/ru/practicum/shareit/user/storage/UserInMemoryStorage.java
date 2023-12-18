package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exceptions.ResourceConflictException;
import ru.practicum.shareit.util.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class storage for users in memory
 */
@Component("userInMemoryStorage")
public class UserInMemoryStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    private static int id = 0; //  Unique ID for every user

    /**
     * Add users to the storage
     *
     * @param user to add
     * @return added user
     */
    @Override
    public User addUser(User user) {
        checkUsersEmail(user);
        user.setId(++id);
        users.put(id, user);
        return users.get(id);
    }

    /**
     * Update user on the storage
     *
     * @param user to update
     * @return updated user
     */
    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ResourceNotFoundException("No such user!");
        }

        User userFromStorage = users.get(user.getId());
        if (user.getName() != null) {
            userFromStorage.setName(user.getName());
        }
        if (user.getEmail() != null) {
            checkUsersEmail(user);
            userFromStorage.setEmail(user.getEmail());
        }
        users.put(user.getId(), userFromStorage);
        return users.get(user.getId());
    }

    /**
     * Delete user by id
     *
     * @param id of user
     */
    @Override
    public void deleteUser(int id) {
        if (!users.containsKey(id)) {
            throw new ResourceNotFoundException("No such user with that id!");
        }

        users.remove(id);
    }

    /**
     * Return user by id
     *
     * @param id of user
     * @return user
     */
    @Override
    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            throw new ResourceNotFoundException("No such user with that id!");
        }

        return users.get(id);
    }

    /**
     * Return list with all users
     *
     * @return list with all users
     */
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Checking user's email on duplication
     * except user to update
     *
     * @param user to check email
     */
    private void checkUsersEmail(User user) {
        String userEmail = user.getEmail();
        boolean isExist = false;
        for (User value : users.values()) {
            if ((value.getEmail().equals(userEmail)) && (user.getId() != value.getId())) {
                isExist = true;
                break;
            }
        }

        if (isExist) {
            throw new ResourceConflictException("User with that email is already exists!");
        }
    }
}
