package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.JuniorDeveloper;
import ru.otus.hw.domain.MiddleDeveloper;
import ru.otus.hw.domain.SeniorDeveloper;
import ru.otus.hw.domain.Speed;

import java.util.HashSet;
import java.util.Set;
import java.util.random.RandomGenerator;

@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopmentServiceImpl implements DevelopmentService {

    private static final String[] HARD_SKILLS = {"Hibernate", "Spring", "Docker", "Maven"};

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    public MiddleDeveloper stepTransformationToMiddle(JuniorDeveloper junior) {
        log.info("Start middle transformation {}", junior.name());
        var middleDeveloper = new MiddleDeveloper(junior.name(),
                junior.experience() + 3,
                Set.of("Java core", "base sql"),
                Speed.MEDIUM);
        log.info("{}", middleDeveloper);
        log.info("End middle transformation {}", middleDeveloper.name());
        return middleDeveloper;
    }

    public SeniorDeveloper stepTransformationToSenior(MiddleDeveloper middle) {
        log.info("Start senior transformation {}", middle.name());
        var seniorDeveloper = new SeniorDeveloper(middle.name(),
                middle.experience() + 3,
                getHardSkills(middle.hardSkills()),
                Speed.FAST,
                randomGenerator.nextBoolean());
        log.info("{}", seniorDeveloper);
        log.info("End senior transformation {}", seniorDeveloper.name());
        return seniorDeveloper;
    }

    private Set<String> getHardSkills(Set<String> hardSkills) {
        var newHardSkills = new HashSet<>(hardSkills);
        newHardSkills.add(HARD_SKILLS[randomGenerator.nextInt(HARD_SKILLS.length)]);
        newHardSkills.add(HARD_SKILLS[randomGenerator.nextInt(HARD_SKILLS.length)]);
        newHardSkills.add(HARD_SKILLS[randomGenerator.nextInt(HARD_SKILLS.length)]);
        return newHardSkills;
    }
}
