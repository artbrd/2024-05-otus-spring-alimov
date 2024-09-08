package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {BookController.class})
@Import(SecurityConfiguration.class)
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private BookConverter bookConverter;

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void getAllBookAccessAllowed() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    void getAllBookForbidden() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void createBookAccessAllowed() throws Exception {
        mockMvc.perform(get("/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/createBook"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void createBookForbidden() throws Exception {
        mockMvc.perform(get("/create"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void editBookAccessAllowed() throws Exception {
        given(bookService.findById(anyLong())).willReturn(Optional.of(new BookDto()));
        given(bookConverter.toBookEditDto(any())).willReturn(new BookEditDto());

        mockMvc.perform(get("/edit/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name("books/editBook"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void editBookForbidden() throws Exception {
        mockMvc.perform(get("/edit/{id}", 2L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void updateBookAccessAllowed() throws Exception {
        mockMvc.perform(post("/edit"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void updateBookForbidden() throws Exception {
        mockMvc.perform(post("/edit"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void saveBookInsertAccessAllowed() throws Exception {
        mockMvc.perform(post("/save"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void saveBookInsertForbidden() throws Exception {
        mockMvc.perform(post("/save"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void deleteBookAccessAllowed() throws Exception {
        mockMvc.perform(post("/delete/{id}", 1L))
                .andExpect(status().isFound());
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void deleteBookForbidden() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        mockMvc.perform(post("/delete/{id}", 1L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/forbidden"));
    }
}