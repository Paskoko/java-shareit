package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository mockUserRepository;
    @InjectMocks
    UserServiceImpl userService;

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
    void createUser() {
        when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void updateUser() {
        userDto.setName("new");
        when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(user));
        when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(UserMapper.toUser(userDto));

        UserDto result = userService.updateUser(userDto, userDto.getId());

        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void getUserById() {
        when(mockUserRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(user));

        UserDto result = userService.getUserById(userDto.getId());

        assertNotNull(result);
        assertEquals(userDto, result);

    }

    @Test
    void getAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);

        when(mockUserRepository.findAll())
                .thenReturn(userList);

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(userDtoList, result);
    }
}