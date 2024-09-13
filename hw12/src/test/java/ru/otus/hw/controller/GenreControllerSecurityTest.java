package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.services.GenreService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {GenreController.class})
@Import(SecurityConfiguration.class)
class GenreControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GenreService genreService;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void getAllGenres() throws Exception {
        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres/allGenres"));
    }

    @Test
    void getAllGenresUnauthorizedRedirectLogin() throws Exception {
        mockMvc.perform(get("/genres"))
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(status().isFound());
    }
}