package ru.practicum.shareit.item.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exceptions.ItemHeaderException;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class service for operations with items storage
 */
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserService userService;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }


    /**
     * Add new item with validation
     *
     * @param itemDto to add
     * @param userId  of owner
     * @return added item
     */
    @Override
    public ItemDto createItem(ItemDto itemDto, String userId) {
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("No available field!");
        }
        validateUserId(userId);
        itemDto.setOwnerId(Integer.parseInt(userId));
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
    }

    /**
     * Get item by id with validation
     * add booking info
     *
     * @param itemId of item
     * @param userId of owner
     * @return item
     */
    @Override
    public ItemBookingDto getItemById(int itemId, String userId) {
        validateUserId(userId);
        ItemBookingDto itemBookingDto = ItemMapper.toItemBookingDto(itemRepository.findById(itemId).orElseThrow());
        if (Integer.parseInt(userId) == itemBookingDto.getOwnerId()) {
            itemBookingDto.setLastBooking(getLastBookingForItem(itemId));
            itemBookingDto.setNextBooking(getNextBookingForItem(itemId));
        }
        itemBookingDto.setComments(CommentMapper.toListCommentDto(commentRepository.findByItem_Id(itemId)));
        return itemBookingDto;
    }

    /**
     * Get list of all user's items
     * add booking info for all items
     *
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param userId of owner
     * @return list of user's items
     */
    @Override
    public List<ItemBookingDto> getAllItems(Integer from, Integer size, String userId) {
        validateUserId(userId);

        List<Item> itemList;

        if ((from == null) || (size == null)) {
            itemList = itemRepository.findByOwnerId(Integer.parseInt(userId));
        } else {
            if ((from < 0) || (size <= 0)) {
                throw new ValidationException("Parameters should be natural!");
            }
            PageRequest page = PageRequest.of(from / size, size);
            itemList = itemRepository.findByOwnerId(Integer.parseInt(userId), page)
                    .stream()
                    .collect(Collectors.toList());
        }

        return ItemMapper.toListItemBookingDto(itemList)
                .stream()
                .peek(itemBookingDto -> {
                    itemBookingDto.setLastBooking(getLastBookingForItem(itemBookingDto.getId()));
                    itemBookingDto.setNextBooking(getNextBookingForItem(itemBookingDto.getId()));
                    itemBookingDto.setComments(CommentMapper.toListCommentDto(
                            commentRepository.findByItem_Id(itemBookingDto.getId())));
                })
                .collect(Collectors.toList());
    }

    /**
     * Update item with validation
     *
     * @param itemDto item to update
     * @param itemId  of item
     * @param userId  of owner
     * @return updated item
     */
    @Override
    public ItemDto updateItem(ItemDto itemDto, int itemId, String userId) {
        validateUserId(userId);
        itemDto.setId(itemId);
        itemDto.setOwnerId(Integer.parseInt(userId));
        Item itemFromRep = itemRepository.findById(itemId).orElseThrow();
        itemFromRep = itemFromRep.compare(ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(itemRepository.save(itemFromRep));
    }

    /**
     * Search for items
     *
     * @param text   to search
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param userId of user
     * @return list of found items
     */
    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchItems(String text, Integer from, Integer size, String userId) {
        validateUserId(userId);

        List<Item> searchItems = new ArrayList<>();
        if (text.isEmpty()) {
            return ItemMapper.toListItemDto(searchItems);
        }

        BooleanExpression byIsAvailable = QItem.item.isAvailable.isTrue();
        BooleanExpression byName = QItem.item.name.containsIgnoreCase(text);
        BooleanExpression byDescription = QItem.item.description.containsIgnoreCase(text);


        if ((from == null) || (size == null)) {
            searchItems = (List<Item>) itemRepository.findAll(byIsAvailable.and(byName.or(byDescription)));
        } else {
            if ((from < 0) || (size <= 0)) {
                throw new ValidationException("Parameters should be natural!");
            }
            PageRequest page = PageRequest.of(from / size, size);
            searchItems = itemRepository.findAll(byIsAvailable.and(byName.or(byDescription)), page)
                    .stream()
                    .collect(Collectors.toList());
        }

        return ItemMapper.toListItemDto(searchItems);
    }


    /**
     * Add comment to the item
     *
     * @param commentDto to add
     * @param itemId     of item
     * @param userId     of user
     * @return comment
     */
    @Override
    public CommentDto addComment(CommentDto commentDto, int itemId, String userId) {
        validateUserId(userId);

        BooleanExpression byBooker = QBooking.booking.booker.id.eq(Integer.parseInt(userId));
        BooleanExpression byState = QBooking.booking.item.id.eq(itemId)
                .and(QBooking.booking.state.ne(BookingState.REJECTED)
                        .and(QBooking.booking.end.before(LocalDateTime.now())));

        List<Booking> bookings = (List<Booking>) bookingRepository.findAll(byBooker.and(byState));

        if (bookings.isEmpty()) {
            throw new ValidationException("No booking for this item/user");
        }
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(ItemMapper.toItem(getItemById(itemId, userId)));
        comment.setAuthor(UserMapper.toUser(userService.getUserById(Integer.parseInt(userId))));

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    /**
     * Validation of item's owner
     *
     * @param userId of owner
     */
    private void validateUserId(String userId) {
        if (userId == null) {
            throw new ItemHeaderException("No header with user id!");
        }

        int id = Integer.parseInt(userId);

        userService.getUserById(id);    // Validation of user id
    }

    /**
     * Get last booking for item
     *
     * @param itemId of item
     * @return last booking
     */
    private BookingItemDto getLastBookingForItem(int itemId) {
        List<Booking> bookingList = bookingRepository.findByItem_IdAndStartIsBefore(itemId, LocalDateTime.now(),
                Sort.by(Sort.Direction.DESC, "end"));
        if (!bookingList.isEmpty()) {
            return bookingList.stream()
                    .findFirst()
                    .map(BookingMapper::toBookingItemDto)
                    .orElse(null);
        }
        return null;
    }

    /**
     * Get next booking for item
     *
     * @param itemId of item
     * @return next booking
     */
    private BookingItemDto getNextBookingForItem(int itemId) {
        List<Booking> bookingList = bookingRepository.findByItem_IdAndStartIsAfter(itemId, LocalDateTime.now(),
                Sort.by(Sort.Direction.ASC, "start"));
        if (!bookingList.isEmpty()) {
            return bookingList.stream()
                    .filter(booking -> !booking.getState().equals(BookingState.REJECTED))
                    .findFirst()
                    .map(BookingMapper::toBookingItemDto)
                    .orElse(null);
        }
        return null;
    }
}
