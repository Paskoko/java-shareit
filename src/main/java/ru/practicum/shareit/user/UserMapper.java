package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class mapper for user object
 */
public class UserMapper {

    /**
     * Transform user to userDto object
     *
     * @param user to transform
     * @return userDto object
     */
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Transform userDto to user object
     *
     * @param userDto to transform
     * @param id      of user object
     * @return user object
     */
    public static User toUser(UserDto userDto, int id) {
        return User.builder()
                .id(id)
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    /**
     * Transform list of user to list of userDto objects
     *
     * @param userList to transform
     * @return list of userDto objects
     */
    public static List<UserDto> toListUserDto(List<User> userList) {
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
