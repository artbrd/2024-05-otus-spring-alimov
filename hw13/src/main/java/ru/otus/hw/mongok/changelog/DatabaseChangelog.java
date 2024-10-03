package ru.otus.hw.mongok.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "drop", author = "alimovae", runAlways = true)
    public void drop(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "init", author = "alimovae")
    public void init(MongoAuthorRepository mongoAuthorRepository, MongoBookRepository mongoBookRepository) {
        MongoAuthor author1 = mongoAuthorRepository.save(new MongoAuthor("1", "Author_1"));
        MongoAuthor author2 = mongoAuthorRepository.save(new MongoAuthor("2", "Author_2"));
        MongoAuthor author3 = mongoAuthorRepository.save(new MongoAuthor("3", "Author_3"));

        MongoBook book1 = mongoBookRepository.save(new MongoBook("1", "BookTitle_1", author1));
        MongoBook book2 = mongoBookRepository.save(new MongoBook("2", "BookTitle_2", author2));
        MongoBook book3 = mongoBookRepository.save(new MongoBook("3", "BookTitle_3", author3));
        MongoBook book4 = mongoBookRepository.save(new MongoBook("4", "BookTitle_4", author1));
    }
}
