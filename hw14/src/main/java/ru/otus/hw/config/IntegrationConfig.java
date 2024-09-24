package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import ru.otus.hw.domain.SeniorDeveloper;
import ru.otus.hw.services.DevelopmentService;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> juniorChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> middleChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow developerFlow(DevelopmentService developmentService) {
        return IntegrationFlow.from(juniorChannel())
                .split()
                .handle(developmentService, "stepTransformationToMiddle")
                .<SeniorDeveloper>log(LoggingHandler.Level.INFO, "MiddleDeveloper", Message::getPayload)
                .handle(developmentService, "stepTransformationToSenior")
                .<SeniorDeveloper>log(LoggingHandler.Level.INFO, "SeniorDeveloper", Message::getPayload)
                .aggregate()
                .channel(middleChannel())
                .get();
    }
}
