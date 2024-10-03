package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.JuniorDeveloper;
import ru.otus.hw.domain.Speed;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {

    private static final List<String> NAMES = List.of("Joe", "Alex", "Bob", "Jack", "Jennie");

    private final JuniorGateway juniorGateway;

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    public void startGenerateJuniorDeveloper() {
        ForkJoinPool.commonPool().execute(() -> {
            List<JuniorDeveloper> junior = generateJuniorJavaDevelopers();
            juniorGateway.process(junior);
        });
    }

    private List<JuniorDeveloper> generateJuniorJavaDevelopers() {
        var developerCount = 5;
        List<JuniorDeveloper> juniors = new ArrayList<>(developerCount);
        for (int i = 0; i < developerCount; i++) {
            juniors.add(generateJuniorDeveloper());
        }
        return juniors;
    }

    private JuniorDeveloper generateJuniorDeveloper() {
        int experience = randomGenerator.nextInt(0, 4);
        int indexName = randomGenerator.nextInt(0, NAMES.size());
        return new JuniorDeveloper(NAMES.get(indexName), experience, Speed.SLOW);
    }
}
