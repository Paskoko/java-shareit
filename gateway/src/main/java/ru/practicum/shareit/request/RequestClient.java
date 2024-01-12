package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.util.exceptions.ItemHeaderException;

import java.util.Map;

/**
 * Class client to prepare REST requests
 */
@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${sv.url}") String serverUrl, RestTemplateBuilder builder) {
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
     * @param requestDto to create
     * @param userId     of creator
     * @return response from server
     */
    public ResponseEntity<Object> createRequest(RequestDto requestDto, String userId) {
        checkUserId(userId);
        return post("", Integer.parseInt(userId), requestDto);
    }

    /**
     * Handle get all user requests
     *
     * @param userId of user
     * @return response from server
     */
    public ResponseEntity<Object> getUserRequests(String userId) {
        checkUserId(userId);
        return get("", Integer.parseInt(userId));
    }


    /**
     * Handle get request by id
     *
     * @param requestId of request
     * @param userId    of owner
     * @return response from server
     */
    public ResponseEntity<Object> getRequestById(int requestId, String userId) {
        checkUserId(userId);
        return get("/" + requestId, Integer.parseInt(userId));
    }

    /**
     * Handle get all requests with parameters
     *
     * @param from   index of the first element
     * @param size   of the page
     * @param userId of owner
     * @return response from server
     */
    public ResponseEntity<Object> getAllRequests(Integer from, Integer size, String userId) {
        checkUserId(userId);
        if ((from != null) && (size != null)) {
            Map<String, Object> parameters = Map.of(
                    "from", from,
                    "size", size
            );
            return get("/all?from={from}&size={size}", Integer.parseInt(userId), parameters);
        } else {
            return get("/all", Integer.parseInt(userId));
        }
    }

    /**
     * Validation of item's owner
     *
     * @param userId of owner
     */
    private void checkUserId(String userId) {
        if (userId == null) {
            throw new ItemHeaderException("No header with user id!");
        }
    }
}
