package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Class controller for items
 */
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;


    /**
     * POST request handler with validation
     *
     * @param itemDto to add
     * @param userId  user id
     * @return added item
     */
    @PostMapping()
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto,
                                             @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemClient.createItem(itemDto, userId);
    }

    /**
     * GET item by id request handler
     *
     * @param itemId of item
     * @param userId user id
     * @return item
     */
    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable int itemId,
                                              @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemClient.getItemById(itemId, userId);
    }

    /**
     * GET all items of the user
     *
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param userId user id
     * @return list of all user's items
     */
    @GetMapping()
    public ResponseEntity<Object> getAllItems(
            @PositiveOrZero @RequestParam(value = "from", required = false) Integer from,
            @Positive @RequestParam(value = "size", required = false) Integer size,
            @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemClient.getAllItems(from, size, userId);
    }

    /**
     * PUT request handler with validation
     *
     * @param itemDto item to update
     * @param itemId  of item
     * @param userId  user id
     * @return updated item
     */
    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                                             @PathVariable int itemId,
                                             @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemClient.updateItem(itemDto, itemId, userId);

    }

    /**
     * GET search request handler
     *
     * @param text   to search
     * @param from   index of the first element
     * @param size   number of elements to return
     * @param userId user id
     * @return list of found items
     */
    @GetMapping(value = "/search")
    public ResponseEntity<Object> search(
            @RequestParam(value = "text") String text,
            @PositiveOrZero @RequestParam(value = "from", required = false) Integer from,
            @Positive @RequestParam(value = "size", required = false) Integer size,
            @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemClient.searchItems(text, from, size, userId);
    }


    /**
     * POST add comment request handler
     *
     * @param commentDto comment to add
     * @param itemId     of item
     * @param userId     user id
     * @return added comment
     */
    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto commentDto,
                                             @PathVariable int itemId,
                                             @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemClient.addComment(commentDto, itemId, userId);
    }
}
