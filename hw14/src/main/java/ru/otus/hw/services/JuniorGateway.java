package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.JuniorDeveloper;
import ru.otus.hw.domain.SeniorDeveloper;

import java.util.List;

@MessagingGateway
public interface JuniorGateway {

    @Gateway(requestChannel  = "juniorChannel", replyChannel = "seniorChannel")
    List<SeniorDeveloper> process(List<JuniorDeveloper> juniorDevelopers);
}
