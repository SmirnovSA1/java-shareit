package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CommentMapperTest {
    @Autowired
    private CommentMapper commentMapper;

    @Test
    void toComment() {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Some text")
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .text("Some text")
                .build();

        Comment result = commentMapper.toComment(commentDto);

        assertEquals(comment, result);
    }

    @Test
    void toCommentDtoResponse() {
        Comment comment = Comment.builder()
                .id(1L)
                .text("Some text")
                .build();

        CommentDtoResponse commentDtoResponse = CommentDtoResponse.builder()
                .id(1L)
                .text("Some text")
                .build();

        CommentDtoResponse result = commentMapper.toCommentDtoResponse(comment);
        result.setCreated(null);

        assertEquals(commentDtoResponse, result);
    }

    @Test
    void toListCommentDtoResponse() {
        Comment comment1 = Comment.builder()
                .id(1L)
                .text("Some text 1")
                .build();

        Comment comment2 = Comment.builder()
                .id(2L)
                .text("Some text 2")
                .build();

        List<Comment> comments = List.of(comment1, comment2);

        CommentDtoResponse commentDtoResponse1 = CommentDtoResponse.builder()
                .id(1L)
                .text("Some text 1")
                .build();

        CommentDtoResponse commentDtoResponse2 = CommentDtoResponse.builder()
                .id(2L)
                .text("Some text 2")
                .build();

        List<CommentDtoResponse> commentsDtoResponse = List.of(commentDtoResponse1, commentDtoResponse2);

        List<CommentDtoResponse> result = commentMapper.toListCommentDtoResponse(comments);
        result.forEach(c -> {
            c.setCreated(null);
        });

        assertEquals(commentsDtoResponse, result);
    }
}
