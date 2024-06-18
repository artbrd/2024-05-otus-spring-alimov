package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.Application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Application.class)
@TestPropertySource(locations = "classpath:/application-test.properties")
@RequiredArgsConstructor
class CsvQuestionDaoTest {

    private final QuestionDao questionDao;

    @Test
    void findAll() {
        var questions = questionDao.findAll();

        assertAll(
                () -> assertThat(questions).isNotEmpty(),
                () -> assertThat(questions.size()).isPositive(),
                () -> questions.forEach(question -> assertNotNull(question.text())),
                () -> questions.forEach(question -> assertThat(question.answers()).isNotEmpty()),
                () -> questions.forEach(question -> question.answers().forEach(answer -> assertNotNull(answer.text())))
        );
    }
}