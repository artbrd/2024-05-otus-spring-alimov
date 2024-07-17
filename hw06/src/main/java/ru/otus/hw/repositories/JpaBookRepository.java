package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        try {
            EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-graph");
            return Optional.ofNullable(entityManager.find(Book.class, id, Map.of(FETCH.getKey(), entityGraph)));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        try {
            EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-graph");
            var query = entityManager.createQuery("SELECT b FROM Book b LEFT JOIN FETCH b.genres", Book.class);
            query.setHint(FETCH.getKey(), entityGraph);
            return query.getResultList();
        } catch (Exception ex) {
            log.error("Exception:", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(long id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
        }
    }
}
