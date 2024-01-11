package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.model.Booking;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for bookings table
 * with queryDSL support
 */
public interface BookingRepository extends JpaRepository<Booking, Integer>, QuerydslPredicateExecutor<Booking> {
    /**
     * Query to database to get list of all bookings
     * for item with start before now
     * sorted
     *
     * @param itemId of item
     * @param start  time limit
     * @param sort   sorted type
     * @return list of bookings
     */
    @Transactional
    List<Booking> findByItem_IdAndStartIsBefore(int itemId, LocalDateTime start, Sort sort);

    /**
     * Query to database to get list of all bookings
     * for item with start after now
     * sorted
     *
     * @param itemId of item
     * @param start  time limit
     * @param sort   sorted type
     * @return list of bookings
     */
    @Transactional
    List<Booking> findByItem_IdAndStartIsAfter(int itemId, LocalDateTime start, Sort sort);


}
