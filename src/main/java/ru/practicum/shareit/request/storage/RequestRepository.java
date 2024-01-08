package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

/**
 * Repository interface for requests table
 * with queryDSL support
 */
public interface RequestRepository extends JpaRepository<Request, Integer>, QuerydslPredicateExecutor<Request> {

    /**
     * Query to database to get all requests of a user
     *
     * @param requester id of owner
     * @return list of user's requests
     */
    List<Request> findByRequester(int requester);
}
