package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    private final Student student = new Student("Petr", "Petrov");
    private final List<Answer> answers = List.of(new Answer("yes", true), new Answer("yes", false));
    private final List<Question> questions = List.of(new Question("Java is a programming language?", answers));

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @BeforeEach
    void before() {
        when(questionDao.findAll()).thenReturn(questions);
    }

    @Test
    void executePositiveTest() {
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), any(), any())).thenReturn(1);

        var testResult = testService.executeTestFor(student);

        assertEquals(1, testResult.getRightAnswersCount());
    }

    @Test
    void executeNegativeTest() {
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), any(), any())).thenReturn(2);

        var testResult = testService.executeTestFor(student);

        assertEquals(0, testResult.getRightAnswersCount());
    }
}