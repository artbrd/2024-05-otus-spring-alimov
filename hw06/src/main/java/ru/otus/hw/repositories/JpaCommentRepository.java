package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<Comment> findById(long id) {
        try {
            return Optional.ofNullable(entityManager.find(Comment.class, id));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        try {
            var query = entityManager.createQuery("SELECT c FROM Comment c WHERE c.book.id = :book_id", Comment.class);
            query.setParameter("book_id", bookId);
            return query.getResultList();
        } catch (Exception ex) {
            log.error("Exception:", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            entityManager.persist(comment);
            return comment;
        }
        return entityManager.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        Comment comment = entityManager.find(Comment.class, id);
        if (comment != null) {
            entityManager.remove(comment);
        }
    }
}
