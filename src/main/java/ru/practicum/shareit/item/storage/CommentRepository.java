package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * Repository interface for comments table
 * with queryDSL support
 */
public interface CommentRepository extends JpaRepository<Comment, Integer>, QuerydslPredicateExecutor<Comment> {
    /**
     * Query to database to get all comments for the item
     *
     * @param itemId of item
     * @return list of all item's comments
     */
    List<Comment> findByItem_Id(int itemId);
}
