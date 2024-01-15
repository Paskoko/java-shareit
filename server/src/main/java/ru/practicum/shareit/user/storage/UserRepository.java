package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.user.model.User;

/**
 * Repository interface for users table
 */
public interface UserRepository extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User> {
}
