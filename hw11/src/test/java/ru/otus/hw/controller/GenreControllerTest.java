package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers = {GenreController.class})
@Import(SecurityConfiguration.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GenreService genreService;

    private List<GenreDto> dbGenres;

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 6).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void getAllGenres() throws Exception {
        given(genreService.findAll()).willReturn(dbGenres);

        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres/allGenres"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", dbGenres));
    }

    @Test
    void getAllGenresUnauthorizedRedirectLogin() throws Exception {
        mockMvc.perform(get("/genres"))
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(status().isFound());
    }
}