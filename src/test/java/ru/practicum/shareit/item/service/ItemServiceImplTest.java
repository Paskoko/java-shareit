package ru.practicum.shareit.item.service;

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
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
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
class ItemServiceImplTest {

    @Mock
    UserService mockUserService;
    @Mock
    ItemRepository mockItemRepository;
    @Mock
    BookingRepository mockBookingRepository;
    @Mock
    CommentRepository mockCommentRepository;
    @InjectMocks
    ItemServiceImpl itemService;


    private static final LocalDateTime start = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime end = LocalDateTime.now().plusDays(2);
    Item item;
    ItemDto itemDto;
    ItemBookingDto itemBookingDto;
    User user;
    UserDto userDto;
    List<Booking> bookingList = new ArrayList<>();
    List<Comment> commentList = new ArrayList<>();
    Booking booking;
    Comment comment;
    String userId;


    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .isAvailable(true)
                .build();
        itemDto = ItemDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .available(true)
                .build();
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

        comment = Comment.builder()
                .id(1)
                .text("test")
                .item(item)
                .author(user)
                .created(start)
                .build();
        booking = Booking.builder()
                .id(1)
                .start(start)
                .end(end)
                .item(item)
                .booker(user)
                .state(BookingState.WAITING)
                .build();
        itemBookingDto = ItemBookingDto.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .available(true)
                .build();
        userId = String.valueOf(user.getId());
    }

    @Test
    void createItem() {
        when(mockUserService.getUserById(user.getId()))
                .thenReturn(userDto);
        when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        ItemDto result = itemService.createItem(itemDto, userId);

        assertNotNull(result);
        assertEquals(itemDto, result);
    }

    @Test
    void checkWrongUserIdValidation() {
        Assertions.assertThrows(ItemHeaderException.class, () ->
                itemService.createItem(itemDto, null));
    }

    @Test
    void createNotAvailableItem() {
        itemDto.setAvailable(null);

        Assertions.assertThrows(ValidationException.class, () ->
                itemService.createItem(itemDto, userId));
    }

    @Test
    void getItemById() {
        bookingList.add(booking);
        commentList.add(comment);
        itemBookingDto.setNextBooking(BookingMapper.toBookingItemDto(bookingList.get(0)));
        itemBookingDto.setLastBooking(BookingMapper.toBookingItemDto(bookingList.get(0)));
        itemBookingDto.setComments(CommentMapper.toListCommentDto(commentList));

        when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        when(mockBookingRepository.findByItem_IdAndStartIsBefore(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingList);
        when(mockBookingRepository.findByItem_IdAndStartIsAfter(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingList);
        when(mockCommentRepository.findByItem_Id(Mockito.anyInt()))
                .thenReturn(commentList);

        ItemBookingDto result = itemService.getItemById(itemDto.getId(), userId);

        assertNotNull(result);
        assertEquals(itemBookingDto, result);
    }

    @Test
    void getAllItemsWithoutPagination() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        List<ItemBookingDto> itemBookingDtoList = new ArrayList<>();
        itemBookingDto.setComments(CommentMapper.toListCommentDto(commentList));
        itemBookingDtoList.add(itemBookingDto);

        when(mockItemRepository.findByOwnerId(Mockito.anyInt()))
                .thenReturn(itemList);
        when(mockBookingRepository.findByItem_IdAndStartIsBefore(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingList);
        when(mockBookingRepository.findByItem_IdAndStartIsAfter(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingList);
        when(mockCommentRepository.findByItem_Id(Mockito.anyInt()))
                .thenReturn(commentList);

        List<ItemBookingDto> result = itemService.getAllItems(null, null, userId);

        assertNotNull(result);
        assertEquals(itemBookingDtoList, result);
    }

    @Test
    void getAllItemsWithoutSize() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        List<ItemBookingDto> itemBookingDtoList = new ArrayList<>();
        itemBookingDto.setComments(CommentMapper.toListCommentDto(commentList));
        itemBookingDtoList.add(itemBookingDto);

        when(mockItemRepository.findByOwnerId(Mockito.anyInt()))
                .thenReturn(itemList);
        when(mockBookingRepository.findByItem_IdAndStartIsBefore(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingList);
        when(mockBookingRepository.findByItem_IdAndStartIsAfter(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingList);
        when(mockCommentRepository.findByItem_Id(Mockito.anyInt()))
                .thenReturn(commentList);

        List<ItemBookingDto> result = itemService.getAllItems(0, null, userId);

        assertNotNull(result);
        assertEquals(itemBookingDtoList, result);
    }

    @Test
    void getAllItemsWrongSize() {
        Assertions.assertThrows(ValidationException.class, () ->
                itemService.getAllItems(0, -2, userId));
    }

    @Test
    void getAllItemsWrongFrom() {
        Assertions.assertThrows(ValidationException.class, () ->
                itemService.getAllItems(-1, 20, userId));
    }

    @Test
    void getAllItemsWithPagination() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        Page<Item> itemPage = new PageImpl<>(itemList);

        List<ItemBookingDto> itemBookingDtoList = new ArrayList<>();
        itemBookingDto.setComments(CommentMapper.toListCommentDto(commentList));
        itemBookingDtoList.add(itemBookingDto);

        when(mockItemRepository.findByOwnerId(Mockito.anyInt(), Mockito.any(Pageable.class)))
                .thenReturn(itemPage);
        when(mockBookingRepository.findByItem_IdAndStartIsBefore(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingList);
        when(mockBookingRepository.findByItem_IdAndStartIsAfter(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingList);
        when(mockCommentRepository.findByItem_Id(Mockito.anyInt()))
                .thenReturn(commentList);

        List<ItemBookingDto> result = itemService.getAllItems(0, 20, userId);

        assertNotNull(result);
        assertEquals(itemBookingDtoList, result);
    }

    @Test
    void updateItem() {
        when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        ItemDto result = itemService.updateItem(itemDto, itemDto.getId(), userId);

        assertNotNull(result);
        assertEquals(itemDto, result);
    }

    @Test
    void searchItemsWithoutPagination() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        List<ItemDto> itemBookingDtoList = new ArrayList<>();
        itemBookingDtoList.add(itemDto);

        when(mockItemRepository.findAll(Mockito.any(BooleanExpression.class)))
                .thenReturn(itemList);

        List<ItemDto> result = itemService.searchItems("item", null, null, userId);

        assertNotNull(result);
        assertEquals(itemBookingDtoList, result);
    }

    @Test
    void searchItemsWithoutSize() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        List<ItemDto> itemBookingDtoList = new ArrayList<>();
        itemBookingDtoList.add(itemDto);

        when(mockItemRepository.findAll(Mockito.any(BooleanExpression.class)))
                .thenReturn(itemList);

        List<ItemDto> result = itemService.searchItems("item", 0, null, userId);

        assertNotNull(result);
        assertEquals(itemBookingDtoList, result);
    }

    @Test
    void searchItemsWithEmptySearch() {
        List<ItemDto> itemBookingDtoList = new ArrayList<>();

        List<ItemDto> result = itemService.searchItems("", null, null, userId);

        assertNotNull(result);
        assertEquals(itemBookingDtoList, result);
    }

    @Test
    void searchItemsWrongSize() {
        Assertions.assertThrows(ValidationException.class, () ->
                itemService.searchItems("item", 0, -2, userId));
    }

    @Test
    void searchItemsWrongFrom() {
        Assertions.assertThrows(ValidationException.class, () ->
                itemService.searchItems("item", -1, 20, userId));
    }

    @Test
    void searchItemsWithPagination() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        Page<Item> itemPage = new PageImpl<>(itemList);

        List<ItemDto> itemBookingDtoList = new ArrayList<>();
        itemBookingDtoList.add(itemDto);

        when(mockItemRepository.findAll(Mockito.any(BooleanExpression.class), Mockito.any(Pageable.class)))
                .thenReturn(itemPage);

        List<ItemDto> result = itemService.searchItems("item", 0, 20, userId);

        assertNotNull(result);
        assertEquals(itemBookingDtoList, result);
    }

    @Test
    void addComment() {
        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        bookingList.add(booking);

        when(mockBookingRepository.findAll(Mockito.any(BooleanExpression.class)))
                .thenReturn(bookingList);
        when(mockCommentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);
        when(mockItemRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(item));
        when(mockBookingRepository.findByItem_IdAndStartIsBefore(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingList);
        when(mockBookingRepository.findByItem_IdAndStartIsAfter(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                Mockito.any()))
                .thenReturn(bookingList);
        when(mockCommentRepository.findByItem_Id(Mockito.anyInt()))
                .thenReturn(commentList);
        when(mockUserService.getUserById(Mockito.anyInt()))
                .thenReturn(userDto);

        CommentDto result = itemService.addComment(commentDto, item.getId(), userId);

        assertNotNull(result);
        assertEquals(commentDto.getId(), result.getId());
        assertEquals(commentDto.getText(), result.getText());
        assertEquals(commentDto.getAuthorName(), result.getAuthorName());
    }

    @Test
    void addCommentForItemWithoutBookings() {
        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        when(mockBookingRepository.findAll(Mockito.any(BooleanExpression.class)))
                .thenReturn(bookingList);

        Assertions.assertThrows(ValidationException.class, () ->
                itemService.addComment(commentDto, item.getId(), userId));
    }
}