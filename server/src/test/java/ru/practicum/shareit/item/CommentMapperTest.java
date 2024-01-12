package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommentMapperTest {

    private static final LocalDateTime created = LocalDateTime.now().plusDays(1);

    CommentDto commentDto;

    @BeforeEach
    void setUp() {
        commentDto = CommentDto.builder()
                .id(1)
                .text("test")
                .authorName("user")
                .created(created)
                .build();
    }

    @Test
    void toCommentDto() {
        Item item = Item.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .isAvailable(true)
                .build();
        User user = User.builder()
                .id(1)
                .name("user")
                .email("user@email.com")
                .build();
        Comment comment = Comment.builder()
                .id(1)
                .text("test")
                .item(item)
                .author(user)
                .created(created)
                .build();

        CommentDto result = CommentMapper.toCommentDto(comment);

        assertNotNull(result);
        assertEquals(commentDto, result);
    }

    @Test
    void toComment() {
        Comment comment = Comment.builder()
                .text("test")
                .created(created)
                .build();

        Comment result = CommentMapper.toComment(commentDto);

        assertNotNull(result);
        assertEquals(comment, result);
    }

    @Test
    void toListCommentDto() {
        Item item = Item.builder()
                .id(1)
                .name("test item")
                .description("test")
                .ownerId(1)
                .isAvailable(true)
                .build();
        User user = User.builder()
                .id(1)
                .name("user")
                .email("user@email.com")
                .build();
        Comment comment = Comment.builder()
                .id(1)
                .text("test")
                .item(item)
                .author(user)
                .created(created)
                .build();
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        List<CommentDto> commentDtoList = new ArrayList<>();
        commentDtoList.add(commentDto);

        List<CommentDto> result = CommentMapper.toListCommentDto(commentList);

        assertNotNull(result);
        assertEquals(commentDtoList, result);
    }
}