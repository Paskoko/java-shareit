package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemController itemController;

    ItemDto itemDto = ItemDto.builder()
            .id(1)
            .name("test item")
            .description("test")
            .ownerId(1)
            .available(true)
            .build();
    ItemBookingDto itemBookingDto = ItemBookingDto.builder()
            .id(1)
            .name("test item")
            .description("test")
            .ownerId(1)
            .available(true)
            .comments(new ArrayList<>())
            .build();


    @Test
    void createItemTest() throws Exception {
        when(itemService.createItem(itemDto, "1"))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId())));
    }

    @Test
    void getItemByIdTest() throws Exception {
        when(itemService.getItemById(1, "1"))
                .thenReturn(itemBookingDto);

        mockMvc.perform(get("/items/{itemId}", 1)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemBookingDto.getId())))
                .andExpect(jsonPath("$.name", is(itemBookingDto.getName())))
                .andExpect(jsonPath("$.description", is(itemBookingDto.getDescription())))
                .andExpect(jsonPath("$.ownerId", is(itemBookingDto.getOwnerId())))
                .andExpect(jsonPath("$.available", is(itemBookingDto.getAvailable())));
    }

    @Test
    void getAllItemsTest() throws Exception {
        List<ItemBookingDto> defaultList = new ArrayList<>();
        defaultList.add(itemBookingDto);
        when(itemService.getAllItems(0, 20, "1"))
                .thenReturn(defaultList);

        mockMvc.perform(get("/items")
                        .contentType("application/json")
                        .param("from", "0")
                        .param("size", "20")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(itemDto, itemDto.getId(), "1"))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", 1)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId())));
    }

    @Test
    void search() throws Exception {
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);
        when(itemService.searchItems("item", 0, 20, "1"))
                .thenReturn(itemDtoList);

        mockMvc.perform(get("/items/search")
                        .contentType("application/json")
                        .param("text", "item")
                        .param("from", "0")
                        .param("size", "20")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("new comment")
                .authorName("user")
                .build();
        when(itemService.addComment(commentDto, itemDto.getId(), "1"))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));

    }
}