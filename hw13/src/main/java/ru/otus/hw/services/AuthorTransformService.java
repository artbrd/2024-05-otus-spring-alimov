package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.repositories.jpa.SequenceRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorTransformService {

    private final Map<String, JpaAuthor> authors;

    private final SequenceRepository sequenceRepository;

    public AuthorTransformService(SequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
        authors = new HashMap<>();
    }

    public void process(MongoAuthor author) {
        authors.put(author.getId(), new JpaAuthor(sequenceRepository.nextVal("author_id_seq"), author.getFullName()));
    }

    public JpaAuthor getAuthor(String mongoId) {
        return this.authors.get(mongoId);
    }
}
