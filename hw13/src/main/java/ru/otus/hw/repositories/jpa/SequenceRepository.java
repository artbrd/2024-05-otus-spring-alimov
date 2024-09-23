package ru.otus.hw.repositories.jpa;

public interface SequenceRepository {

    long nextVal(String name);
}
