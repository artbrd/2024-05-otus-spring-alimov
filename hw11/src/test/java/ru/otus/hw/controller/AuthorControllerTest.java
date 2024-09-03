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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers = {AuthorController.class})
@Import(SecurityConfiguration.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorService authorService;

    private List<AuthorDto> dbAuthors;

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void getAllAuthor() throws Exception {
        given(authorService.findAll()).willReturn(dbAuthors);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors/allAuthors"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", dbAuthors));
    }

    @Test
    void getAllAuthorUnauthorizedRedirectLogin() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(status().isFound());
    }
}