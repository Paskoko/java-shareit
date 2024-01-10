package ru.practicum.shareit.request.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.QRequest;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exceptions.ItemHeaderException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    UserService mockUserService;

    @Mock
    ItemRepository mockItemRepository;

    @Mock
    RequestRepository mockRequestRepository;

    @InjectMocks
    RequestServiceImpl requestService;

    private static final LocalDateTime created = LocalDateTime.now().plusDays(1);
    User user;
    UserDto userDto;
    Request request;
    RequestDto newRequest;
    RequestDto createdRequest;
    List<Request> requestList = new ArrayList<>();
    List<RequestDto> requestDtoList = new ArrayList<>();
    Item item;
    List<Item> items = new ArrayList<>();


    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .name("user")
                .email("user@email.com")
                .build();
        userDto = UserDto.builder()
                .id(1)
                .name("user")
                .email("user@emai.com")
                .build();
        item = Item.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .isAvailable(true)
                .build();
        items.add(item);

        newRequest = RequestDto.builder()
                .description("test")
                .build();
        createdRequest = RequestDto.builder()
                .id(1)
                .description("test")
                .requester(1)
                .build();
        createdRequest.setItems(ItemMapper.toListItemRequestDto(items));
        requestDtoList.add(createdRequest);
        request = Request.builder()
                .id(1)
                .description("test")
                .requester(1)
                .created(created)
                .build();
        requestList.add(request);
    }

    @Test
    void createRequest() {
        String userId = String.valueOf(user.getId());

        when(mockUserService.getUserById(user.getId()))
                .thenReturn(userDto);
        when(mockRequestRepository.save(Mockito.any(Request.class)))
                .thenReturn(request);

        RequestDto result = requestService.createRequest(newRequest, userId);

        assertNotNull(result);
        assertEquals(createdRequest.getId(), result.getId());
        assertEquals(createdRequest.getDescription(), result.getDescription());
        assertEquals(createdRequest.getRequester(), result.getRequester());
    }

    @Test
    void checkWrongUserIdValidation() {
        Assertions.assertThrows(ItemHeaderException.class, () ->
                requestService.createRequest(newRequest, null));
    }

    @Test
    void getUserRequests() {
        String userId = String.valueOf(user.getId());

        when(mockUserService.getUserById(user.getId()))
                .thenReturn(userDto);

        BooleanExpression byRequester = QRequest.request.requester.eq(Integer.parseInt(userId));
        Sort.Direction sort = Sort.Direction.DESC;

        when(mockRequestRepository.findAll(byRequester, Sort.by(sort, "created")))
                .thenReturn(requestList);
        BooleanExpression byRequestId = QItem.item.requestId.eq(request.getId());
        when(mockItemRepository.findAll(byRequestId))
                .thenReturn(items);
        createdRequest.setCreated(created);
        List<RequestDto> result = requestService.getUserRequests(userId);

        assertNotNull(result);
        assertEquals(requestDtoList, result);
    }

    @Test
    void getAllRequestsWithoutPagination() {
        String userId = String.valueOf(user.getId());

        when(mockUserService.getUserById(user.getId()))
                .thenReturn(userDto);
        when(mockRequestRepository.findAll())
                .thenReturn(requestList);
        createdRequest.setCreated(created);
        createdRequest.setItems(null);

        List<RequestDto> result = requestService.getAllRequests(null, null, userId);

        assertNotNull(result);
        assertEquals(requestDtoList, result);
    }

    @Test
    void getAllRequestsWithoutSize() {
        String userId = String.valueOf(user.getId());

        when(mockUserService.getUserById(user.getId()))
                .thenReturn(userDto);
        when(mockRequestRepository.findAll())
                .thenReturn(requestList);
        createdRequest.setCreated(created);
        createdRequest.setItems(null);

        List<RequestDto> result = requestService.getAllRequests(0, null, userId);

        assertNotNull(result);
        assertEquals(requestDtoList, result);
    }

    @Test
    void getAllRequestsWithWrongSize() {
        String userId = String.valueOf(user.getId());

        Assertions.assertThrows(ValidationException.class, () ->
                requestService.getAllRequests(0, -2, userId));
    }

    @Test
    void getAllRequestsWithWrongFrom() {
        String userId = String.valueOf(user.getId());

        Assertions.assertThrows(ValidationException.class, () ->
                requestService.getAllRequests(-1, 20, userId));
    }

    @Test
    void getAllRequestsWithPagination() {
        String userId = String.valueOf(user.getId());

        when(mockUserService.getUserById(user.getId()))
                .thenReturn(userDto);

        int from = 0;
        int size = 20;

        Request newRequest = Request.builder()
                .id(2)
                .description("test")
                .requester(2)
                .created(created)
                .build();
        requestList.add(newRequest);

        Sort.Direction sort = Sort.Direction.DESC;
        PageRequest page = PageRequest.of(from / size, size, Sort.by(sort, "created"));
        Page<Request> requestPage = new PageImpl<>(requestList);

        when(mockRequestRepository.findAll(page))
                .thenReturn(requestPage);
        createdRequest.setId(newRequest.getId());
        createdRequest.setRequester(newRequest.getRequester());
        createdRequest.setCreated(created);

        BooleanExpression expression = QItem.item.requestId.eq(newRequest.getId());
        when(mockItemRepository.findAll(expression))
                .thenReturn(items);

        List<RequestDto> result = requestService.getAllRequests(from, size, userId);

        assertNotNull(result);
        assertEquals(requestDtoList, result);
    }

    @Test
    void getRequestById() {
        String userId = String.valueOf(user.getId());

        when(mockUserService.getUserById(user.getId()))
                .thenReturn(userDto);
        when(mockRequestRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(request));

        BooleanExpression byRequestId = QItem.item.requestId.eq(request.getId());
        when(mockItemRepository.findAll(byRequestId))
                .thenReturn(items);

        RequestDto result = requestService.getRequestById(request.getId(), userId);

        assertNotNull(result);
        assertEquals(createdRequest.getId(), result.getId());
        assertEquals(createdRequest.getDescription(), result.getDescription());
        assertEquals(createdRequest.getRequester(), result.getRequester());
        assertEquals(createdRequest.getItems(), result.getItems());
    }
}