package ru.otus.hw.domain;

import java.util.Set;

public record SeniorDeveloper(String name,
                              int experience,
                              Set<String> hardSkills,
                              Speed speedProgramming,
                              boolean maybeTeamLead) {
}
