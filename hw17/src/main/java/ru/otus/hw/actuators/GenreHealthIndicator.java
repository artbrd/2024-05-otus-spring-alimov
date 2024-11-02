package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.GenreService;

@Component
@RequiredArgsConstructor
public class GenreHealthIndicator implements HealthIndicator {

    private final GenreService genreService;

    @Override
    public Health health() {
        var count = genreService.count();
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
