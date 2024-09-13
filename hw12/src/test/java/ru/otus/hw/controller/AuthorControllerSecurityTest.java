package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AuthorController.class})
@Import(SecurityConfiguration.class)
class AuthorControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorService authorService;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void getAllAuthor() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors/allAuthors"));
    }

    @Test
    void getAllAuthorUnauthorizedRedirectLogin() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(status().isFound());
    }
}