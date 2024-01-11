package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.request.model.Request;

/**
 * Repository interface for requests table
 * with queryDSL support
 */
public interface RequestRepository extends JpaRepository<Request, Integer>, QuerydslPredicateExecutor<Request> {
}
