package ru.practicum.shareit.request.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.QRequest;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class service for operations with request storage
 */
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;


    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserService userService, ItemRepository itemRepository) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    /**
     * Add new request with validation
     *
     * @param requestDto to add
     * @param userId     of owner
     * @return added request
     */
    @Override
    public RequestDto createRequest(RequestDto requestDto, String userId) {
        validateUserId(userId);
        requestDto.setCreated(LocalDateTime.now());
        requestDto.setRequester(Integer.parseInt(userId));
        return RequestMapper.toRequestDto(requestRepository.save(RequestMapper.toRequest(requestDto)));
    }

    /**
     * Get list of all user's requests
     *
     * @param userId of owner
     * @return lists of user's requests
     */
    @Override
    public List<RequestDto> getUserRequests(String userId) {
        validateUserId(userId);

        BooleanExpression byRequester = QRequest.request.requester.eq(Integer.parseInt(userId));
        Sort.Direction sort = Sort.Direction.DESC;
        List<Request> requests = (List<Request>) requestRepository.findAll(byRequester,
                Sort.by(sort, "created"));


        return RequestMapper.toListRequestDto(requests)
                .stream()
                .peek(requestDto -> {
                    BooleanExpression byRequestId = QItem.item.requestId.eq(requestDto.getId());
                    List<Item> items = (List<Item>) itemRepository.findAll(byRequestId);
                    requestDto.setItems(ItemMapper.toListItemRequestDto(items));
                })
                .collect(Collectors.toList());
    }

    /**
     * Get list of all requests
     *
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param userId of user
     * @return list with requests
     */
    @Override
    public List<RequestDto> getAllRequests(Integer from, Integer size, String userId) {
        validateUserId(userId);
        if ((from == null) || (size == null)) {
            return RequestMapper.toListRequestDto(requestRepository.findAll());
        }
        Sort.Direction sort = Sort.Direction.DESC;
        PageRequest page = PageRequest.of(from / size, size, Sort.by(sort, "created"));


        return requestRepository.findAll(page)
                .stream()
                .map(RequestMapper::toRequestDto)
                .filter(requestDto -> requestDto.getRequester() != Integer.parseInt(userId))
                .peek(requestDto -> {
                    BooleanExpression byRequestId = QItem.item.requestId.eq(requestDto.getId());
                    List<Item> items = (List<Item>) itemRepository.findAll(byRequestId);
                    requestDto.setItems(ItemMapper.toListItemRequestDto(items));
                })
                .collect(Collectors.toList());
    }

    /**
     * Get request by id
     *
     * @param requestId of request
     * @param userId    of user
     * @return request
     */
    @Override
    public RequestDto getRequestById(int requestId, String userId) {
        validateUserId(userId);
        RequestDto requestDto = RequestMapper.toRequestDto(requestRepository.findById(requestId).orElseThrow());

        BooleanExpression byRequestId = QItem.item.requestId.eq(requestId);
        List<Item> items = (List<Item>) itemRepository.findAll(byRequestId);

        requestDto.setItems(ItemMapper.toListItemRequestDto(items));
        return requestDto;
    }

    /**
     * Validation of item's owner
     *
     * @param userId of owner
     */
    private void validateUserId(String userId) {
        int id = Integer.parseInt(userId);
        userService.getUserById(id);    // Validation of user id in database
    }
}
