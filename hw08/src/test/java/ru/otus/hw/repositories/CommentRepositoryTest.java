package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DataMongoTest
class CommentRepositoryTest {

    private static final String COMMENT_ID_1 = "1";
    private static final String COMMENT_ID_2 = "2";
    private static final String COMMENT_ID_4 = "4";
    private static final String COMMENT_ID_5 = "5";
    private static final String BOOK_ID_1 = "1";
    private static final String BOOK_ID_2 = "2";
    private static final String COMMENT_ID_100 = "100";

    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    @DisplayName("должен сохранять новый комментарий")
    void shouldSaveNewComment() {
        var relatedBook = mongoOperations.findById(BOOK_ID_1, Book.class);
        var commentToSave = new Comment(null, "Some comment", relatedBook);

        var savedComment = repository.save(commentToSave);
        assertThat(savedComment).isNotNull()
                .matches(b -> b.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(commentToSave);

        var foundComment = mongoOperations.findById(savedComment.getId(), Comment.class);
        assertThat(foundComment).isEqualTo(savedComment);
    }

    @Test
    @DisplayName("должен сохранять измененный комментарий")
    void shouldSaveUpdateComment() {
        var updatedMessage = "New comment";
        var existingComment = mongoOperations.findById(COMMENT_ID_1, Comment.class);

        var commentToUpdate = new Comment(existingComment.getId(), updatedMessage, existingComment.getBook());
        var updated = repository.save(commentToUpdate);

        assertThat(updated).isNotNull()
                .matches(b -> b.getText().equals(updatedMessage));

        assertThat(mongoOperations.findById(updated.getId(), Comment.class)).isEqualTo(updated);
    }

    @Test
    @DisplayName("должен удалять комментарий")
    void shouldDeleteComment() {
        var commentToDelete = mongoOperations.findById(COMMENT_ID_2, Comment.class);
        assertThat(commentToDelete).isNotNull();

        repository.deleteById(COMMENT_ID_2);
        assertThat(mongoOperations.findById(COMMENT_ID_2, Comment.class)).isNull();
    }

    @Nested
    @DirtiesContext(classMode = BEFORE_CLASS)
    class CommentRepositoryFindTest {
        @Test
        @DisplayName("должен загружать комментарий по id")
        void shouldReturnCorrectCommentById() {
            var expected = mongoOperations.findById(COMMENT_ID_1, Comment.class);
            var actual = repository.findById(COMMENT_ID_1);

            assertThat(actual).isPresent()
                    .hasValue(expected);
        }

        @Test
        @DisplayName("должен не загрузить комментарий по несуществующему id")
        void shouldReturnEmptyCommentById() {
            var actual = repository.findById(COMMENT_ID_100);

            assertThat(actual).isNotPresent();
        }

        @Test
        @DisplayName("должен загружать комментарии по идентификатору книги")
        void shouldReturnCorrectCommentListByBookId() {
            var comment4 = mongoOperations.findById(COMMENT_ID_4, Comment.class);
            var comment5 = mongoOperations.findById(COMMENT_ID_5, Comment.class);

            var actual = repository.findAllByBookId(BOOK_ID_2);

            assertThat(actual).containsExactly(comment4, comment5);
        }
    }
}
