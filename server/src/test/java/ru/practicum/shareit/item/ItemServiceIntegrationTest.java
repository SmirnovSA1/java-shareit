package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class ItemServiceIntegrationTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void getAllItemsByOwnerTest() {
        User user = User.builder()
                .name("User")
                .email("userEmail@test.ru")
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

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item1)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking1);

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .item(item2)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking2);

        Comment comment1 = Comment.builder()
                .text("Some text 1")
                .author(user)
                .item(item1)
                .build();
        commentRepository.save(comment1);

        Comment comment2 = Comment.builder()
                .text("Some text 2")
                .author(user)
                .item(item2)
                .build();
        commentRepository.save(comment2);

        List<ItemDtoInfo> items = itemService.getItemsByOwner(user.getId(), 0, 2);

        assertThat(items.size(), equalTo(2));
        assertThat(items.get(0).getLastBooking(), notNullValue());
        assertThat(items.get(1).getNextBooking(), notNullValue());
        assertThat(items.get(0).getComments().size(), equalTo(2));
        assertThat(items.get(1).getComments().size(), equalTo(2));
    }
}
