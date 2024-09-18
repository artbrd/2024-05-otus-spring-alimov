package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.jpa.JpaBook;

import java.util.List;

public interface JpaBookRepository extends JpaRepository<JpaBook, Long> {

    @EntityGraph(attributePaths = "author")
    List<JpaBook> findAll();
}
