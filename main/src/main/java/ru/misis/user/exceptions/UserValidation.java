package ru.misis.user.exceptions;

public class UserValidation extends RuntimeException {
    public UserValidation(String message) {
        super(message);
    }
}
