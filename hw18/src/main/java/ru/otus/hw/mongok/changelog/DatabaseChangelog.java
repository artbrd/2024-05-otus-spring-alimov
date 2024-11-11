package ru.otus.hw.mongok.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "drop", author = "alimovae", runAlways = true)
    public void drop(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "init", author = "alimovae")
    public void init(AuthorRepository authorRepository, GenreRepository genreRepository, BookRepository bookRepository) {
        Author author1 = authorRepository.save(new Author("1", "Author_1")).block();
        Author author2 = authorRepository.save(new Author("2", "Author_2")).block();
        Author author3 = authorRepository.save(new Author("3", "Author_3")).block();

        Genre genre1 = genreRepository.save(new Genre("1", "Genre_1")).block();
        Genre genre2 = genreRepository.save(new Genre("2", "Genre_2")).block();
        Genre genre3 = genreRepository.save(new Genre("3", "Genre_3")).block();
        Genre genre4 = genreRepository.save(new Genre("4", "Genre_4")).block();
        Genre genre5 = genreRepository.save(new Genre("5", "Genre_5")).block();

        Book book1 = bookRepository.save(new Book("1", "BookTitle_1", author1, genre1)).block();
        Book book2 = bookRepository.save(new Book("2", "BookTitle_2", author2, genre2)).block();
        Book book3 = bookRepository.save(new Book("3", "BookTitle_3", author3, genre3)).block();
    }
}
