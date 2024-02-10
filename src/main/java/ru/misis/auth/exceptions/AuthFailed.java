package ru.misis.auth.exceptions;

public class AuthFailed extends RuntimeException {
    public AuthFailed(String message) {
        super(message);
    }
}
