package ru.otus.hw.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.mongo.MongoAuthor;

import java.util.HashMap;
import java.util.Map;

@Getter
@Service
public class AuthorTransformService {

    private final Map<String, JpaAuthor> authors;

    public AuthorTransformService() {
        authors = new HashMap<>();
    }

    public void process(MongoAuthor author) {
        authors.put(author.getId(), new JpaAuthor(authors.size() + 1, author.getFullName()));
    }
}
