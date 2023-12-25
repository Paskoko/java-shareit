package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Repository interface for items table
 * with queryDSL support
 */
public interface ItemRepository extends JpaRepository<Item, Integer>, QuerydslPredicateExecutor<Item> {
    /**
     * Query to database to get all items of a user
     *
     * @param ownerId of item
     * @return list of user's items
     */
    List<Item> findByOwnerId(int ownerId);
}
