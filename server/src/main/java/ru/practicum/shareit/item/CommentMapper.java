package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class mapper for item object
 */
public class CommentMapper {
    /**
     * Transform from comment to commentDto object
     *
     * @param comment to transform
     * @return commentDto object
     */
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }


    /**
     * Transform from commentDto to comment object
     *
     * @param commentDto to transform
     * @return comment object
     */
    public static Comment toComment(CommentDto commentDto) {
        return Comment.builder()
                .text(commentDto.getText())
                .created(commentDto.getCreated())
                .build();
    }

    /**
     * Transform list of comment to list of commentDto objects
     *
     * @param commentList to transform
     * @return list of commentDto objects
     */
    public static List<CommentDto> toListCommentDto(List<Comment> commentList) {
        return commentList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
