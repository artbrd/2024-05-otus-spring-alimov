package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class TestServiceImplTest {

    private final Student student = new Student("Petr", "Petrov");
    private final List<Answer> answers = List.of(new Answer("yes", true), new Answer("yes", false));
    private final List<Question> questions = List.of(new Question("Java is a programming language?", answers));

    @Autowired
    private TestServiceImpl testService;

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @BeforeEach
    void before() {
        when(questionDao.findAll()).thenReturn(questions);
    }

    @Test
    void executePositiveTest() {
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), any(), any())).thenReturn(1);

        var testResult = testService.executeTestFor(student);

        assertEquals(1, testResult.getRightAnswersCount());
    }

    @Test
    void executeNegativeTest() {
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), any(), any())).thenReturn(2);

        var testResult = testService.executeTestFor(student);

        assertEquals(0, testResult.getRightAnswersCount());
    }
}