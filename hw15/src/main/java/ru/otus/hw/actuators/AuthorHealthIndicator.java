package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.AuthorRepository;

@Component
@RequiredArgsConstructor
public class AuthorHealthIndicator implements HealthIndicator {

    private final AuthorRepository authorRepository;

    @Override
    public Health health() {
        var count = authorRepository.count();
        if (count > 0) {
            return Health.up()
                    .withDetail("message", "Есть кому писать книги")
                    .build();
        }
        return Health.down()
                .withDetail("message", "Некому писать книги!")
                .build();
    }
}
