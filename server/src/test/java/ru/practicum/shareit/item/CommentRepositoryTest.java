package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByItemId() {
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

        Comment comment = Comment.builder()
                .text("Some text")
                .item(item)
                .author(user)
                .build();
        commentRepository.save(comment);

        List<Comment> result = commentRepository.findAllByItemId(1L);

        assertNotNull(result);
    }

    @Test
    void findAllByItemIn() {
        User user = User.builder()
                .name("User")
                .email("userEmail@test.com")
                .build();
        userRepository.save(user);

        Item item1 = Item.builder()
                .name("Item 1")
                .description("Описание 1")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
                .name("Item 2")
                .description("Описание 2")
                .available(true)
                .owner(user)
                .build();
        itemRepository.save(item2);

        Comment comment1 = Comment.builder()
                .text("Some text 1")
                .item(item1)
                .author(user)
                .build();
        commentRepository.save(comment1);

        Comment comment2 = Comment.builder()
                .text("Some text 2")
                .item(item1)
                .author(user)
                .build();
        commentRepository.save(comment2);

        Comment comment3 = Comment.builder()
                .text("Some text 3")
                .item(item2)
                .author(user)
                .build();
        commentRepository.save(comment3);

        List<Comment> result = commentRepository.findAllByItemIn(List.of(item1));

        assertNotNull(result);
    }
}