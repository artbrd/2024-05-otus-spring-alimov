package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.converter.QuestionConverter;
import ru.otus.hw.dao.QuestionDao;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConverter questionConverter;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        var questions = questionDao.findAll();
        ioService.printLine(questionConverter.questionToString(questions));
    }
}
