package ru.otus.hw.converter;

import ru.otus.hw.domain.Question;

import java.util.List;

public class QuestionConverterImpl implements QuestionConverter {
    @Override
    public String questionToString(List<Question> questions) {
        var stringBuilder = new StringBuilder();
        int numberQuestion = 1;
        int numberAnswer = 1;
        for (var question : questions) {
            stringBuilder.append("%d. %s".formatted(numberQuestion++, question.text()));
            stringBuilder.append(System.lineSeparator());
            numberAnswer = 1;
            for (var answer : question.answers()) {
                stringBuilder.append("   %d. %s".formatted(numberAnswer++, answer.text()));
                stringBuilder.append(System.lineSeparator());
            }
        }
        return stringBuilder.toString();
    }
}
