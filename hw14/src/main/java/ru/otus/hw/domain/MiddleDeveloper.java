package ru.otus.hw.domain;

import java.util.Set;

public record MiddleDeveloper(String name,
                              int experience,
                              Set<String> hardSkills,
                              Speed speedProgramming) {
}
