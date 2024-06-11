package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoResponse;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class CommentSerializationTest {
    @Autowired
    private JacksonTester<CommentDto> toJsonTesterCommentDto;
    @Autowired
    private JacksonTester<CommentDto> toDtoTesterCommentDto;
    @Autowired
    private JacksonTester<CommentDtoResponse> toJsonTesterCommentDtoResponse;
    @Autowired
    private JacksonTester<CommentDtoResponse> toDtoTesterCommentDtoResponse;

    @Test
    void dtoSerializationTest() throws IOException {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Some text")
                .build();

        JsonContent<CommentDto> result1 = toJsonTesterCommentDto.write(commentDto);

        assertThat(result1).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result1).extractingJsonPathStringValue("$.text").isEqualTo("Some text");

        LocalDateTime now = LocalDateTime.now();
        CommentDtoResponse commentDtoResponse = CommentDtoResponse.builder()
                .id(1L)
                .text("Some text")
                .authorName("Автор")
                .created(now)
                .build();

        JsonContent<CommentDtoResponse> result2 = toJsonTesterCommentDtoResponse.write(commentDtoResponse);

        assertThat(result2).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result2).extractingJsonPathStringValue("$.text").isEqualTo("Some text");
        assertThat(result2).extractingJsonPathStringValue("$.authorName").isEqualTo("Автор");
        assertThat(result2).extractingJsonPathStringValue("$.created").isNotEmpty();
    }

    @Test
    void dtoDeserializationTest() throws IOException {
        String json1 = "{ \"id\":\"1\", \"text\":\"Some text\"}";
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Some text")
                .build();

        CommentDto result1 = toDtoTesterCommentDto.parseObject(json1);

        assertEquals(commentDto, result1);

        LocalDateTime now = LocalDateTime.now();
        String nowString = now.toString();
        String json2 = "{ \"id\":\"1\", \"text\":\"Some text\", \"authorName\":\"Автор\", \"created\":\"" + nowString + "\"}";
        CommentDtoResponse commentDtoResponse = CommentDtoResponse.builder()
                .id(1L)
                .text("Some text")
                .authorName("Автор")
                .created(now)
                .build();

        CommentDtoResponse result2 = toDtoTesterCommentDtoResponse.parseObject(json2);

        assertEquals(commentDtoResponse, result2);
    }
}
