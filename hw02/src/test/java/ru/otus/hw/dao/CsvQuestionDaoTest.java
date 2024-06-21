package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvQuestionDaoTest {

    private QuestionDao questionDao;

    @BeforeEach
    void setUp() {
        var properties = new AppProperties(1, "questions-test.csv");
        questionDao= new CsvQuestionDao(properties);
    }

    @Test
    void findAll() {
        var expectedQuestion = new Question("Java is a programming language?",
                List.of(new Answer("yes", true),
                        new Answer("no", false)));
        var questions = questionDao.findAll();

        assertThat(questions).containsExactlyInAnyOrder(expectedQuestion);
    }
}