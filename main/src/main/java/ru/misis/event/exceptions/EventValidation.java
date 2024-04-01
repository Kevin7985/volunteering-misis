package ru.misis.event.exceptions;

public class EventValidation extends RuntimeException {
    public EventValidation(String message) {
        super(message);
    }
}
