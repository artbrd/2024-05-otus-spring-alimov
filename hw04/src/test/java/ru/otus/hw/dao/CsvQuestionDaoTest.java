package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.LocaleConfig;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @MockBean
    private TestFileNameProvider testFileNameProvider;

    @MockBean
    private LocaleConfig localeConfig;

    @MockBean
    private TestConfig testConfig;

    @Test
    void findAll() {
        var expectedQuestion = new Question("Java is a programming language?",
                List.of(new Answer("yes", true),
                        new Answer("no", false)));

        when(testFileNameProvider.getTestFileName()).thenReturn("questions-test.csv");
        when(localeConfig.getLocale()).thenReturn(Locale.forLanguageTag("en-US"));
        when(testConfig.getRightAnswersCountToPass()).thenReturn(1);

        var questions = questionDao.findAll();

        assertThat(questions).containsExactlyInAnyOrder(expectedQuestion);
    }
}