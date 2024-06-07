package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.converter.QuestionConverter;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private QuestionConverter questionConverter;

    @Test
    void executeTest() {
        var answers = List.of(new Answer("yes", true), new Answer("yes", false));
        var questions = List.of(new Question("Java is a programming language?", answers));

        when(questionDao.findAll()).thenReturn(questions);
        when(questionConverter.questionToString(questions)).thenReturn(anyString());

        testService.executeTest();

        verify(ioService, times(2)).printLine(anyString());
        verify(ioService, times(1)).printFormattedLine(anyString());
        verify(questionConverter, times(1)).questionToString(questions);
    }
}