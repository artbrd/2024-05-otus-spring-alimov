package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreConverter {

    public GenreDto toDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        return genreDto;
    }
}
