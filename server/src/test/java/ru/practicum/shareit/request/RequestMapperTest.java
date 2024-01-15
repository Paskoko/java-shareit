package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RequestMapperTest {

    private static final LocalDateTime created = LocalDateTime.now().plusDays(1);

    RequestDto requestDto;
    Request request;

    @BeforeEach
    void setUp() {
        requestDto = RequestDto.builder()
                .id(1)
                .description("test")
                .requester(1)
                .created(created)
                .build();

        request = Request.builder()
                .id(1)
                .description("test")
                .requester(1)
                .created(created)
                .build();
    }

    @Test
    void toRequestDto() {
        RequestDto result = RequestMapper.toRequestDto(request);

        assertNotNull(result);
        assertEquals(requestDto, result);
    }

    @Test
    void toRequest() {
        Request result = RequestMapper.toRequest(requestDto);

        assertNotNull(result);
        assertEquals(request, result);
    }

    @Test
    void toListRequestDto() {
        List<Request> requests = new ArrayList<>();
        requests.add(request);
        List<RequestDto> requestDtoList = new ArrayList<>();
        requestDtoList.add(requestDto);

        List<RequestDto> result = RequestMapper.toListRequestDto(requests);

        assertNotNull(result);
        assertEquals(requestDtoList, result);
    }
}