package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final int MIN_ANSWER_NUMBER = 1;

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printFormattedLine("%s", question.text());
            var isAnswerValid = printAnswerAndGetUserAnswer(question.answers()); // Задать вопрос, получить ответ
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean printAnswerAndGetUserAnswer(List<Answer> answers) {
        var answerMap = answerListToMap(answers);
        printAnswer(answerMap);
        return readUserAnswer(answerMap);
    }

    private Map<Integer, Answer> answerListToMap(List<Answer> answers) {
        return IntStream
                .range(1, answers.size() + 1)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), i -> answers.get(i - 1)));
    }

    private void printAnswer(Map<Integer, Answer> answerMap) {
        answerMap.forEach((key, value) -> ioService.printFormattedLine("%d. %s", key, value.text()));
    }

    private boolean readUserAnswer(Map<Integer, Answer> answerMap) {
        var userAnswer = ioService.readIntForRangeWithPromptLocalized(MIN_ANSWER_NUMBER,
                answerMap.size(),
                "TestService.chose.answer",
                "TestService.error.message.answer");
        return answerMap.get(userAnswer).isCorrect();
    }
}
