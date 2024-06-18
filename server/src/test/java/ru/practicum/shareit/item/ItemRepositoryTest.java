package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void searchAvailableItem() {
        User user = User.builder()
                .name("User")
                .email("userEmail@test.com")
                .build();
        userRepository.save(user);

        Item item = Item.builder()
                .name("Item")
                .description("Описание")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item);

        String text = "опИсаНие";
        Pageable pageable = PageRequest.of(0, 10);

        List<Item> items = itemRepository.findByNameContainingIgnoreCase(text, pageable);

        assertNotNull(items);
        assertFalse(items.isEmpty());

        items.forEach(i -> {
            assertTrue(i.getAvailable());
            assertTrue(i.getName().toLowerCase().contains(text) ||
                    i.getDescription().toLowerCase().contains(text.toLowerCase()));
        });
        userRepository.delete(user);
        itemRepository.delete(item);
    }
}
