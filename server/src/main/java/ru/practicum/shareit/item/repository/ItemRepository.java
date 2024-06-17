package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select it " +
            "from Item it " +
            "where (lower(it.name) like lower(concat('%', ?1, '%')) " +
            "or lower(it.description) like lower(concat('%', ?1, '%'))) " +
            "and it.available = true")
    List<Item> findByNameContainingIgnoreCase(String nameSearch, Pageable pageable);

    List<Item> findByOwnerIdOrderById(Long ownerId, Pageable pageable);

    List<Item> findAllByRequestIdIn(List<Long> requestIdList);

    List<Item> findAllByRequestId(Long requestId);
}
