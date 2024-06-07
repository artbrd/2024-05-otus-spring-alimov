package ru.otus.hw.converter;

import ru.otus.hw.domain.Question;

import java.util.List;

public interface QuestionConverter {
    String questionToString(List<Question> questions);
}
