package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {LoginController.class})
@Import(SecurityConfiguration.class)
class LoginControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void failPage() throws Exception {
        mockMvc.perform(post("/login-fail"))
                .andExpect(status().isOk());
    }

    @Test
    void forbidden() throws Exception {
        mockMvc.perform(get("/forbidden"))
                .andExpect(status().isOk());
    }
}