package ru.otus.hw.mongok.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "drop", author = "alimovae", runAlways = true)
    public void drop(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "init", author = "alimovae")
    public void init(AuthorRepository authorRepository, GenreRepository genreRepository,
                     BookRepository bookRepository, CommentRepository commentRepository) {
        Author author1 = authorRepository.save(new Author("1", "Author_1"));
        Author author2 = authorRepository.save(new Author("2", "Author_2"));
        Author author3 = authorRepository.save(new Author("3", "Author_3"));

        Genre genre1 = genreRepository.save(new Genre("1", "Genre_1"));
        Genre genre2 = genreRepository.save(new Genre("2", "Genre_2"));
        Genre genre3 = genreRepository.save(new Genre("3", "Genre_3"));
        Genre genre4 = genreRepository.save(new Genre("4", "Genre_4"));
        Genre genre5 = genreRepository.save(new Genre("5", "Genre_5"));

        Book book1 = bookRepository.save(new Book("1", "BookTitle_1", author1, List.of(genre1, genre2)));
        Book book2 = bookRepository.save(new Book("2", "BookTitle_2", author2, List.of(genre3, genre4)));
        Book book3 = bookRepository.save(new Book("3", "BookTitle_3", author3, List.of(genre5)));

        commentRepository.save(new Comment("1", "Comment_1", book1));
        commentRepository.save(new Comment("2", "Comment_2", book1));
        commentRepository.save(new Comment("3", "Comment_3", book1));
        commentRepository.save(new Comment("4", "Comment_4", book2));
        commentRepository.save(new Comment("5", "Comment_5", book2));
        commentRepository.save(new Comment("6", "Comment_6", book3));
    }
}
