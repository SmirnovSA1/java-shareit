package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByRequesterIdOrderByCreatedDesc() {
        User user1 = User.builder()
                .name("User 1")
                .email("user1Email@test.com")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .name("User 2")
                .email("user2Email@test.com")
                .build();
        userRepository.save(user2);

        ItemRequest itemRequest1 = ItemRequest.builder()
                .description("Описание 1")
                .created(LocalDateTime.now())
                .requester(user1)
                .build();
        itemRequestRepository.save(itemRequest1);

        ItemRequest itemRequest2 = ItemRequest.builder()
                .description("Описание 2")
                .created(LocalDateTime.now())
                .requester(user1)
                .build();
        itemRequestRepository.save(itemRequest2);

        ItemRequest itemRequest3 = ItemRequest.builder()
                .description("Описание 3")
                .created(LocalDateTime.now())
                .requester(user2)
                .build();
        itemRequestRepository.save(itemRequest3);

        List<ItemRequest> result = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void findAllByRequesterIdNot() {
        User user1 = User.builder()
                .name("User 1")
                .email("user1Email@test.com")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .name("User 2")
                .email("user2Email@test.com")
                .build();
        userRepository.save(user2);

        ItemRequest itemRequest1 = ItemRequest.builder()
                .description("Описание 1")
                .created(LocalDateTime.now())
                .requester(user1)
                .build();
        itemRequestRepository.save(itemRequest1);

        ItemRequest itemRequest2 = ItemRequest.builder()
                .description("Описание 2")
                .created(LocalDateTime.now())
                .requester(user1)
                .build();
        itemRequestRepository.save(itemRequest2);

        ItemRequest itemRequest3 = ItemRequest.builder()
                .description("Описание 3")
                .created(LocalDateTime.now())
                .requester(user2)
                .build();
        itemRequestRepository.save(itemRequest3);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("created").descending());
        List<ItemRequest> result = itemRequestRepository.findAllByRequesterIdNot(1L, pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}