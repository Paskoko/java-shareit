package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceIntegrationTest {

    private final RequestService requestService;
    private final UserService userService;

    @Test
    void createRequestTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@emai.com")
                .build();
        UserDto createdUser = userService.createUser(userDto);
        String userId = String.valueOf(createdUser.getId());

        RequestDto newRequest = RequestDto.builder()
                .description("test")
                .build();

        RequestDto createdRequest = RequestDto.builder()
                .description("test")
                .requester(createdUser.getId())
                .build();

        RequestDto result = requestService.createRequest(newRequest, userId);

        assertThat(result.getDescription()).isEqualTo(createdRequest.getDescription());
        assertThat(result.getRequester()).isEqualTo(createdRequest.getRequester());
    }

    @Test
    void getUserRequestsTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@emai.com")
                .build();
        UserDto createdUser = userService.createUser(userDto);
        String userId = String.valueOf(createdUser.getId());

        RequestDto newRequest = RequestDto.builder()
                .description("test")
                .build();

        RequestDto createdRequest = requestService.createRequest(newRequest, userId);
        createdRequest.setItems(new ArrayList<>());
        List<RequestDto> requestDtoList = new ArrayList<>();
        requestDtoList.add(createdRequest);

        List<RequestDto> result = requestService.getUserRequests(userId);

        assertThat(requestDtoList).isEqualTo(result);
    }

    @Test
    void getRequestByIdTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@emai.com")
                .build();
        UserDto createdUser = userService.createUser(userDto);
        String userId = String.valueOf(createdUser.getId());
        RequestDto newRequest = RequestDto.builder()
                .description("test")
                .build();

        RequestDto createdRequest = requestService.createRequest(newRequest, userId);
        createdRequest.setItems(new ArrayList<>());

        RequestDto result = requestService.getRequestById(createdRequest.getId(), userId);

        assertThat(createdRequest).isEqualTo(result);
    }

    @Test
    void getAllRequestsTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@emai.com")
                .build();
        UserDto createdUser = userService.createUser(userDto);
        String userId = String.valueOf(createdUser.getId());

        RequestDto newRequest = RequestDto.builder()
                .description("test")
                .build();
        RequestDto createdRequest = requestService.createRequest(newRequest, userId);
        List<RequestDto> requestDtoList = new ArrayList<>();
        requestDtoList.add(createdRequest);

        List<RequestDto> result = requestService.getAllRequests(null, null, userId);

        assertThat(requestDtoList).isEqualTo(result);

    }
}
