package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@Component
public class CommentConverter {
    public String commentToString(CommentDto comment) {
        return "Id: %s, text: %s".formatted(comment.getId(), comment.getText());
    }

    public CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        return commentDto;
    }
}
