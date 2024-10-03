package ru.otus.hw.exceptions;

public class SequenceNotFoundException extends RuntimeException {
    public SequenceNotFoundException(String message) {
        super(message);
    }
}
