package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;
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
    @Transactional
    List<Item> findByOwnerId(int ownerId);

    /**
     * Query to database to get all items of a user
     *
     * @param ownerId  of item
     * @param pageable page parameters
     * @return list of user's items
     */
    @Transactional
    Page<Item> findByOwnerId(int ownerId, Pageable pageable);
}
