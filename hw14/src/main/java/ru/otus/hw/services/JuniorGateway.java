package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.JuniorDeveloper;
import ru.otus.hw.domain.MiddleDeveloper;

import java.util.List;

@MessagingGateway
public interface JuniorGateway {

    @Gateway(requestChannel  = "juniorChannel", replyChannel = "middleChannel")
    List<MiddleDeveloper> process(List<JuniorDeveloper> juniorDevelopers);
}
