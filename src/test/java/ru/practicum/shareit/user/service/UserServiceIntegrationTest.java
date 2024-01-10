package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceIntegrationTest {
    private final UserService userService;

    @Test
    void createUserTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@mail.com")
                .build();

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void updateUserTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@mail.com")
                .build();

        userDto = userService.createUser(userDto);
        userDto.setName("new");

        UserDto result = userService.updateUser(userDto, userDto.getId());

        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void getUserByIdTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@mail.com")
                .build();

        userDto = userService.createUser(userDto);

        UserDto result = userService.getUserById(userDto.getId());

        assertNotNull(result);
        assertEquals(userDto, result);

    }

    @Test
    void deleteUserTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@mail.com")
                .build();

        userDto = userService.createUser(userDto);

        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);

        UserDto newUser = UserDto.builder()
                .name("new")
                .email("new@mail.com")
                .build();
        newUser = userService.createUser(newUser);


        userService.deleteUser(newUser.getId());
        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(userDtoList, result);
    }

    @Test
    void getAllUsersTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@mail.com")
                .build();

        userDto = userService.createUser(userDto);

        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(userDtoList, result);
    }
}
