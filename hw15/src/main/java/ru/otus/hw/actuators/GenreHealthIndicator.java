package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.GenreRepository;

@Component
@RequiredArgsConstructor
public class GenreHealthIndicator implements HealthIndicator {

    private final GenreRepository genreRepository;

    @Override
    public Health health() {
        var count = genreRepository.count();
        if (count > 0) {
            return Health.up()
                    .withDetail("message", "Жанры для книг есть")
                    .build();
        }
        return Health.down()
                .withDetail("message", "Отсутствуют жанры для книг!")
                .build();
    }
}
