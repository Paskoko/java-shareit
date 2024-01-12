package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.ValidationException;
import java.util.Map;

import static ru.practicum.shareit.util.Util.checkUserId;

/**
 * Class client to prepare REST requests
 */
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${sv.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }


    /**
     * Handle create request
     *
     * @param itemDto to add
     * @param userId  user id
     * @return response from server
     */
    public ResponseEntity<Object> createItem(ItemDto itemDto, String userId) {
        checkUserId(userId);
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("No available field!");
        }
        return post("", Integer.parseInt(userId), itemDto);
    }

    /**
     * Handle get item by id request
     *
     * @param itemId of item
     * @param userId user id
     * @return response from server
     */
    public ResponseEntity<Object> getItemById(int itemId, String userId) {
        checkUserId(userId);
        return get("/" + itemId, Integer.parseInt(userId));
    }


    /**
     * Handle get all items with parameters
     *
     * @param from   index of the first element
     * @param size   of the page
     * @param userId of owner
     * @return response from server
     */
    public ResponseEntity<Object> getAllItems(Integer from, Integer size, String userId) {
        checkUserId(userId);
        if ((from != null) && (size != null)) {
            Map<String, Object> parameters = Map.of(
                    "from", from,
                    "size", size
            );
            return get("?from={from}&size={size}", Integer.parseInt(userId), parameters);
        } else {
            return get("", Integer.parseInt(userId));
        }
    }


    /**
     * Handle update request
     *
     * @param itemDto to update
     * @param itemId  of item
     * @param userId  of user
     * @return response from server
     */
    public ResponseEntity<Object> updateItem(ItemDto itemDto, int itemId, String userId) {
        checkUserId(userId);
        return patch("/" + itemId, Integer.parseInt(userId), itemDto);
    }


    /**
     * Handle search with parameters
     *
     * @param text   to search
     * @param from   index of the first element
     * @param size   of the page
     * @param userId of owner
     * @return response from server
     */
    public ResponseEntity<Object> searchItems(String text, Integer from, Integer size, String userId) {
        checkUserId(userId);
        if ((from != null) && (size != null)) {
            Map<String, Object> parameters = Map.of(
                    "text", text,
                    "from", from,
                    "size", size
            );
            return get("/search?text={text}&from={from}&size={size}", Integer.parseInt(userId), parameters);
        } else {
            Map<String, Object> parameters = Map.of(
                    "text", text);
            return get("/search?text={text}", Integer.parseInt(userId), parameters);
        }
    }

    /**
     * Handle add comment request
     *
     * @param commentDto to add
     * @param itemId     of item
     * @param userId     of user
     * @return response from server
     */
    public ResponseEntity<Object> addComment(CommentDto commentDto, int itemId, String userId) {
        checkUserId(userId);
        return post("/" + itemId + "/comment", Integer.parseInt(userId), commentDto);
    }
}
