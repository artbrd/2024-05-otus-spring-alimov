package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaAuthorRepository implements AuthorRepository {

    private final EntityManager entityManager;

    @Override
    public List<Author> findAll() {
        try {
            return entityManager.createQuery("SELECT a FROM Author a", Author.class).getResultList();
        } catch (Exception ex) {
            log.error("Exception:", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            return Optional.ofNullable(entityManager.find(Author.class, id));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
