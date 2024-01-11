package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * Class controller for items
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * POST request handler with validation
     *
     * @param item   to add
     * @param userId user id
     * @return added item
     */
    @PostMapping()
    public ItemDto createItem(@Valid @RequestBody ItemDto item,
                              @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemService.createItem(item, userId);
    }

    /**
     * GET item by id request handler
     *
     * @param itemId of item
     * @param userId user id
     * @return item
     */
    @GetMapping(value = "/{itemId}")
    public ItemBookingDto getItemById(@PathVariable int itemId,
                                      @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemService.getItemById(itemId, userId);
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
    public List<ItemBookingDto> getAllItems(
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemService.getAllItems(from, size, userId);
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
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable int itemId,
                              @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemService.updateItem(itemDto, itemId, userId);

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
    public List<ItemDto> search(
            @RequestParam(value = "text") String text,
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemService.searchItems(text, from, size, userId);
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
    public CommentDto addComment(@Valid @RequestBody CommentDto commentDto,
                                 @PathVariable int itemId,
                                 @RequestHeader(value = "X-Sharer-User-Id") String userId) {
        return itemService.addComment(commentDto, itemId, userId);
    }
}
