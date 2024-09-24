package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.domain.JuniorDeveloper;
import ru.otus.hw.domain.Speed;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JuniorGatewayTest {

    @Autowired
    private JuniorGateway juniorGateway;

    @Test
    void process() {
        var juniorDevelopers = List.of(new JuniorDeveloper("Test", 1, Speed.SLOW));
        var actual = juniorGateway.process(juniorDevelopers);

        assertThat(actual).isNotEmpty();
        assertThat(actual.size()).isOne();
    }
}