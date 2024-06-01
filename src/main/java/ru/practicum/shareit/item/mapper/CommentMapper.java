package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "item", ignore = true)
    @Mapping(target = "author", ignore = true)
    Comment toComment(CommentDto commentDto);

    @Mapping(target = "authorName", source = "comment.author.name")
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    CommentDtoResponse toCommentDtoResponse(Comment comment);
}
