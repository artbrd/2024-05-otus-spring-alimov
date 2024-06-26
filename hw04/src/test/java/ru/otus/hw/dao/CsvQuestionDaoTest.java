package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @Test
    void findAll() {
        var expectedQuestion = new Question("Java is a programming language?",
                List.of(new Answer("yes", true),
                        new Answer("no", false)));
        var questions = questionDao.findAll();

        assertThat(questions).containsExactlyInAnyOrder(expectedQuestion);
    }
}