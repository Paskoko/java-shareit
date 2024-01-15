package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * Class client to prepare REST requests
 */
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${sv.url}") String serverUrl, RestTemplateBuilder builder) {
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
     * @param userDto to create
     * @return response from server
     */
    public ResponseEntity<Object> createUser(UserDto userDto) {
        return post("", userDto);
    }

    /**
     * Handle get user request
     *
     * @param userId of user to get
     * @return response from server
     */
    public ResponseEntity<Object> getUserById(Integer userId) {
        return get("/" + userId, userId);
    }

    /**
     * Handle update request
     *
     * @param userDto to update
     * @param userId  of user
     * @return response from server
     */
    public ResponseEntity<Object> updateUser(UserDto userDto, Integer userId) {
        return patch("/" + userId, userId, userDto);
    }

    /**
     * Handle delete request
     *
     * @param userId of user to delete
     * @return response from server
     */
    public ResponseEntity<Object> deleteUser(Integer userId) {
        return delete("/" + userId, userId);
    }

    /**
     * Handle get all users request
     *
     * @return response from server
     */
    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }
}
