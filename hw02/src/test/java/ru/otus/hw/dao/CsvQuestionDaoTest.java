package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvQuestionDaoTest {

    private final QuestionDao questionDao = new CsvQuestionDao(new AppProperties(1, "questions-test.csv"));

    @Test
    void findAll() {
        var expectedQuestion = "Java is a programming language?";
        var questions = questionDao.findAll();

        assertAll(
                () -> assertThat(questions).isNotEmpty(),
                () -> assertThat(questions.size()).isOne(),
                () -> assertThat(questions.get(0).text()).isEqualTo(expectedQuestion),
                () -> assertThat(questions.get(0).answers()).isNotEmpty(),
                () -> assertThat(questions.get(0).answers().size()).isEqualTo(2),
                () -> assertTrue(questions.get(0).answers().containsAll(List.of(new Answer("yes", true), new Answer("no", false))))
        );
    }
}