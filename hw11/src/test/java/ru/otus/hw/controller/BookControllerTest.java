package ru.otus.hw.controller;

import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {BookController.class})
@Import(SecurityConfiguration.class)
class BookControllerTest {

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

    private List<AuthorDto> dbAuthors;

    private List<GenreDto> dbGenres;

    private List<BookDto> dbBooks;

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 6).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        var partition = ListUtils.partition(dbGenres, 2);
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id, "BookTitle_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
                .toList();
    }

    private static List<BookDto> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void getAllBook() throws Exception {
        given(bookService.findAll()).willReturn(dbBooks);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/allBooks"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", dbBooks));
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void createBook() throws Exception {
        given(authorService.findAll()).willReturn(dbAuthors);
        given(genreService.findAll()).willReturn(dbGenres);

        mockMvc.perform(get("/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/createBook"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("authors", dbAuthors))
                .andExpect(model().attribute("genres", dbGenres));
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
    void editBook() throws Exception {
        var book = dbBooks.stream().filter(val -> val.getId() == 2L).findFirst();
        var bookEditDto = new BookEditDto(book.get().getId(),
                book.get().getTitle(),
                book.get().getAuthor().getId(),
                book.get().getGenre().getId());

        given(authorService.findAll()).willReturn(dbAuthors);
        given(genreService.findAll()).willReturn(dbGenres);
        given(bookService.findById(anyLong())).willReturn(book);
        given(bookConverter.toBookEditDto(any())).willReturn(bookEditDto);

        mockMvc.perform(get("/edit/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name("books/editBook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", bookEditDto))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", dbAuthors))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", dbGenres));
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
    void updateBook() throws Exception {
        var author = dbAuthors.stream().filter(val -> val.getId() == 2L).findFirst().get();
        var genre = dbGenres.stream().filter(val -> val.getId() == 4L).findFirst().get();
        var request = new BookEditDto(10, "BookTitle_10", author.getId(), genre.getId());
        var bookDto = new BookDto(request.getId(), request.getTitle(), author, genre);

        given(bookService.update(anyLong(), anyString(), anyLong(), anyLong())).willReturn(bookDto);

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
    void saveBookInsert() throws Exception {
        var author = dbAuthors.stream().filter(val -> val.getId() == 2L).findFirst().get();
        var genre = dbGenres.stream().filter(val -> val.getId() == 4L).findFirst().get();
        var request = new BookEditDto(10, "BookTitle_10", author.getId(), genre.getId());
        var bookDto = new BookDto(request.getId(), request.getTitle(), author, genre);

        var newBooks = new ArrayList<>(dbBooks);
        newBooks.add(bookDto);

        given(bookService.insert(anyString(), anyLong(), anyLong())).willReturn(bookDto);
        given(bookService.findAll()).willReturn(newBooks);

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
    void deleteBook() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        mockMvc.perform(post("/delete/{id}", 1L))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
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