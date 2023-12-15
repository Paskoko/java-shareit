package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.servlet.http.HttpServletRequest;
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
     * @param item    to add
     * @param request to get user id
     * @return added item
     */
    @PostMapping()
    public ItemDto createItem(@Valid @RequestBody ItemDto item, HttpServletRequest request) {
        String userId = request.getHeader("X-Sharer-User-Id");
        return itemService.createItem(item, userId);
    }

    /**
     * GET item by id request handler
     *
     * @param itemId  of item
     * @param request to get user id
     * @return item
     */
    @GetMapping(value = "/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId, HttpServletRequest request) {
        String userId = request.getHeader("X-Sharer-User-Id");
        return itemService.getItemById(itemId, userId);
    }

    /**
     * GET all items of the user
     *
     * @param request to get user id
     * @return list of all user's items
     */
    @GetMapping()
    public List<ItemDto> getAllItems(HttpServletRequest request) {
        String userId = request.getHeader("X-Sharer-User-Id");
        return itemService.getAllItems(userId);
    }

    /**
     * PUT request handler with validation
     *
     * @param itemDto item to update
     * @param itemId  of item
     * @param request to get user id
     * @return updated item
     */
    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable int itemId, HttpServletRequest request) {
        String userId = request.getHeader("X-Sharer-User-Id");
        return itemService.updateItem(itemDto, itemId, userId);

    }

    /**
     * GET search request handler
     *
     * @param text    to search
     * @param request to get user id
     * @return list of found items
     */
    @GetMapping(value = "/search")
    public List<ItemDto> search(@RequestParam(value = "text") String text, HttpServletRequest request) {
        String userId = request.getHeader("X-Sharer-User-Id");
        return itemService.searchItems(text, userId);
    }


}
