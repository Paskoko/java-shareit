package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegrationTest {

    private static final LocalDateTime created = LocalDateTime.now();

    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;


    @Test
    void createItemTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@emai.com")
                .build();
        UserDto createdUser = userService.createUser(userDto);
        String userId = String.valueOf(createdUser.getId());

        ItemDto itemDto = ItemDto.builder()
                .name("test item")
                .description("test")
                .ownerId(createdUser.getId())
                .available(true)
                .build();

        ItemDto result = itemService.createItem(itemDto, userId);

        assertThat(result.getName()).isEqualTo(itemDto.getName());
        assertThat(result.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(result.getOwnerId()).isEqualTo(itemDto.getOwnerId());
        assertThat(result.getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(result.getRequestId()).isEqualTo(itemDto.getRequestId());
        assertThat(result.getComments()).isEqualTo(itemDto.getComments());
    }

    @Test
    void getItemByIdTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@emai.com")
                .build();
        UserDto createdUser = userService.createUser(userDto);
        String userId = String.valueOf(createdUser.getId());

        ItemDto itemDto = ItemDto.builder()
                .name("test item")
                .description("test")
                .ownerId(createdUser.getId())
                .available(true)
                .build();

        itemDto = itemService.createItem(itemDto, userId);

        ItemBookingDto itemBookingDto = ItemBookingDto.builder()
                .id(itemDto.getId())
                .name("test item")
                .description("test")
                .ownerId(createdUser.getId())
                .available(true)
                .comments(new ArrayList<>())
                .build();

        ItemBookingDto result = itemService.getItemById(itemDto.getId(), userId);

        assertThat(result).isEqualTo(itemBookingDto);
    }

    @Test
    void getAllItemsTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@emai.com")
                .build();
        UserDto createdUser = userService.createUser(userDto);
        String userId = String.valueOf(createdUser.getId());

        ItemDto itemDto = ItemDto.builder()
                .name("test item")
                .description("test")
                .ownerId(createdUser.getId())
                .available(true)
                .build();

        itemDto = itemService.createItem(itemDto, userId);

        ItemBookingDto itemBookingDto = ItemBookingDto.builder()
                .id(itemDto.getId())
                .name("test item")
                .description("test")
                .ownerId(createdUser.getId())
                .available(true)
                .comments(new ArrayList<>())
                .build();
        List<ItemBookingDto> defaultList = new ArrayList<>();
        defaultList.add(itemBookingDto);

        List<ItemBookingDto> result = itemService.getAllItems(null, null, userId);

        assertThat(result).isEqualTo(defaultList);
    }

    @Test
    void updateItemTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@emai.com")
                .build();
        UserDto createdUser = userService.createUser(userDto);
        String userId = String.valueOf(createdUser.getId());

        ItemDto itemDto = ItemDto.builder()
                .name("test item")
                .description("test")
                .ownerId(createdUser.getId())
                .available(true)
                .build();

        itemDto = itemService.createItem(itemDto, userId);

        itemDto.setDescription("new");

        ItemDto result = itemService.updateItem(itemDto, itemDto.getId(), userId);

        assertThat(result).isEqualTo(itemDto);
    }

    @Test
    void searchItemsTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@emai.com")
                .build();
        UserDto createdUser = userService.createUser(userDto);
        String userId = String.valueOf(createdUser.getId());

        ItemDto itemDto = ItemDto.builder()
                .name("test item")
                .description("test")
                .ownerId(createdUser.getId())
                .available(true)
                .build();

        itemDto = itemService.createItem(itemDto, userId);

        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);

        List<ItemDto> result = itemService.searchItems("item", null, null, userId);

        assertThat(result).isEqualTo(itemDtoList);

        List<ItemDto> wrong = itemService.searchItems("new", null, null, userId);

        assertThat(wrong).isNotEqualTo(itemDtoList);
    }

    @Test
    void addCommentTest() {
        UserDto userDto = UserDto.builder()
                .name("user")
                .email("user@emai.com")
                .build();
        UserDto createdUser = userService.createUser(userDto);
        String userId = String.valueOf(createdUser.getId());

        UserDto bookerDto = UserDto.builder()
                .name("booker")
                .email("booker@emai.com")
                .build();
        bookerDto = userService.createUser(bookerDto);

        ItemDto itemDto = ItemDto.builder()
                .name("test item")
                .description("test")
                .ownerId(createdUser.getId())
                .available(true)
                .build();

        itemDto = itemService.createItem(itemDto, userId);

        CommentDto commentDto = CommentDto.builder()
                .text("new comment")
                .authorName(createdUser.getName())
                .build();

        BookingPostDto bookingPostDto = BookingPostDto.builder()
                .itemId(itemDto.getId())
                .start(created.plusHours(1))
                .end(created.plusHours(2))
                .build();

        bookingService.createBooking(bookingPostDto, String.valueOf(bookerDto.getId()));

        final int itemId = itemDto.getId();
        final int bookerId = bookerDto.getId();

        Assertions.assertThrows(ValidationException.class, () ->
                itemService.addComment(commentDto, itemId, String.valueOf(bookerId)));
    }
}
