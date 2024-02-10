package ru.misis.skills.exceptions;

public class SkillNotFound extends RuntimeException {
    public SkillNotFound(String message) {
        super(message);
    }
}
