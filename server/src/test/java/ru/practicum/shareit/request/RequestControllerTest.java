package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestController requestController;

    private static final LocalDateTime created = LocalDateTime.now().plusDays(1);

    RequestDto requestDto;
    RequestDto requestDto2;


    @BeforeEach
    void setUp() {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .available(true)
                .requestId(1)
                .build();
        itemRequestDtoList.add(itemRequestDto);

        requestDto = RequestDto.builder()
                .id(1)
                .description("test")
                .requester(1)
                .created(created)
                .items(itemRequestDtoList)
                .build();

        requestDto2 = RequestDto.builder()
                .id(2)
                .description("test")
                .requester(1)
                .created(created.plusHours(1))
                .items(itemRequestDtoList)
                .build();
    }

    @Test
    void createRequestTest() throws Exception {
        when(requestService.createRequest(requestDto, "1"))
                .thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId())))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester())));
    }

    @Test
    void getUserRequestsTest() throws Exception {
        List<RequestDto> requestDtoList = new ArrayList<>();
        requestDtoList.add(requestDto);
        requestDtoList.add(requestDto2);

        when(requestService.getUserRequests("1"))
                .thenReturn(requestDtoList);

        mockMvc.perform(get("/requests")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestByIdTest() throws Exception {
        when(requestService.getRequestById(requestDto.getId(), "1"))
                .thenReturn(requestDto);

        mockMvc.perform(get("/requests/{requestId}", 1)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId())))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.requester", is(requestDto.getRequester())));
    }

    @Test
    void getAllRequestsTest() throws Exception {
        List<RequestDto> requestDtoList = new ArrayList<>();
        requestDtoList.add(requestDto);
        requestDtoList.add(requestDto2);

        when(requestService.getAllRequests(0, 20, "1"))
                .thenReturn(requestDtoList);

        mockMvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .param("from", "0")
                        .param("size", "20")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }
}