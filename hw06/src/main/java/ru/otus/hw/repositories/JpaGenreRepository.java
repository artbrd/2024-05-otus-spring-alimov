package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaGenreRepository implements GenreRepository {

    private final EntityManager entityManager;

    @Override
    public List<Genre> findAll() {
        try {
            return entityManager.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
        } catch (Exception ex) {
            log.error("Exception:", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        try {
            var query = entityManager.createQuery("SELECT g FROM Genre g WHERE g.id in (:ids)", Genre.class);
            query.setParameter("ids", ids);
            return query.getResultList();
        } catch (Exception ex) {
            log.error("Exception:", ex);
            return Collections.emptyList();
        }
    }
}
