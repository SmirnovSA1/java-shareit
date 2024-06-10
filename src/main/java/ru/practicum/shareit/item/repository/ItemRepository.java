package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select * " +
            "from Items as it " +
            "where (upper(it.name) like concat('%', upper(?1), '%') " +
            "or upper(it.description) like concat ('%', upper(?1), '%')) " +
            "and it.available = true ", nativeQuery = true)
    List<Item> findByNameContainingIgnoreCase(String nameSearch, Pageable pageable);

    List<Item> findByOwnerId(Long ownerId, Pageable pageable);

    List<Item> findAllByRequestIdIn(List<Long> requestIdList);

    List<Item> findAllByRequestId(Long requestId);
}
