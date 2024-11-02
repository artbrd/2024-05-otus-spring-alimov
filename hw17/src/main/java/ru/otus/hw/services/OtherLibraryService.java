package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;

public interface OtherLibraryService {

    List<AuthorDto> getAuthors();

    List<GenreDto> getGenres();
}
