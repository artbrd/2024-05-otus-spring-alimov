package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.BookService;

@Component
@RequiredArgsConstructor
public class BookHealthIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        var count = bookService.count();
        if (count > 0) {
            return Health.up()
                    .withDetail("message", "Запасы книг достаточные")
                    .build();
        }
        return Health.down()
                .withDetail("message", "Закончились книги, людям нечего читать!")
                .build();
    }
}
