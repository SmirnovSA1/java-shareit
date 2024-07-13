package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class ItemRequestServiceIntegrationTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void getAllItemRequests() {
        User user1 = User.builder()
                .name("user 1")
                .email("user1email@test.ru")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .name("user 2")
                .email("user2email@test.ru")
                .build();
        userRepository.save(user2);

        ItemRequest itemRequest1 = ItemRequest.builder()
                .description("описание 1")
                .created(LocalDateTime.now())
                .requester(user1)
                .build();
        itemRequestRepository.save(itemRequest1);

        ItemRequest itemRequest2 = ItemRequest.builder()
                .description("описание 2")
                .created(LocalDateTime.now())
                .requester(user2)
                .build();
        itemRequestRepository.save(itemRequest2);

        List<ItemRequestDtoResponse> result = itemRequestService.getAllItemRequests(user1.getId(), 0, 2);

        assertEquals(1, result.size());
        assertEquals(itemRequest2.getDescription(), result.get(0).getDescription());
        assertEquals(itemRequest2.getCreated(), result.get(0).getCreated());
    }
}
