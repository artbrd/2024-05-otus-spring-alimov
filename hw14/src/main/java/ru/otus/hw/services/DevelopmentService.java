package ru.otus.hw.services;

import ru.otus.hw.domain.JuniorDeveloper;
import ru.otus.hw.domain.MiddleDeveloper;
import ru.otus.hw.domain.SeniorDeveloper;

public interface DevelopmentService {

    MiddleDeveloper stepTransformationToMiddle(JuniorDeveloper junior);

    SeniorDeveloper stepTransformationToSenior(MiddleDeveloper middle);
}
