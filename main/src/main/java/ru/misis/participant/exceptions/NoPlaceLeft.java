package ru.misis.participant.exceptions;

public class NoPlaceLeft extends RuntimeException {
    public NoPlaceLeft(String message) {
        super(message);
    }
}
