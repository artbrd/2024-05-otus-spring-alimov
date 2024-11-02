package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.GenreDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class OtherLibraryServiceImpl implements OtherLibraryService {

    private List<AuthorDto> authors;

    private List<GenreDto> genres;

    public OtherLibraryServiceImpl() {
        this.authors = generateAuthors();
        this.genres = generateGenres();
    }

    @Override
    public List<AuthorDto> getAuthors() {
        return authors;
    }

    @Override
    public List<GenreDto> getGenres() {
        return genres;
    }

    private List<AuthorDto> generateAuthors() {
        List<AuthorDto> authors = new ArrayList<>();
        for (int i = 100; i < 110; i++) {
            authors.add(new AuthorDto(i, "Author_" + i));
        }
        return authors;
    }

    private List<GenreDto> generateGenres() {
        List<GenreDto> genres = new ArrayList<>();
        for (int i = 100; i < 110; i++) {
            genres.add(new GenreDto(i, "Genre_" + i));
        }
        return genres;
    }
}
