package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {

    User user = User.builder()
            .id(1)
            .name("user")
            .email("user@mail.com")
            .build();
    UserDto userDto = UserDto.builder()
            .id(1)
            .name("user")
            .email("user@mail.com")
            .build();

    @Test
    void toUserDtoTest() {
        UserDto result = UserMapper.toUserDto(user);

        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void toUserTest() {
        User result = UserMapper.toUser(userDto);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void toListUserDtoTest() {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);
        List<UserDto> result = UserMapper.toListUserDto(userList);

        assertNotNull(result);
        assertEquals(userDtoList, result);
    }
}