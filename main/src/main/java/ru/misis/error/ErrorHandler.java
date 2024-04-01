package ru.misis.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.misis.auth.exceptions.AuthFailed;
import ru.misis.category.exceptions.CategoryNotFound;
import ru.misis.category.exceptions.CategoryValidation;
import ru.misis.error.exceptions.Forbidden;
import ru.misis.error.model.ApiError;
import ru.misis.event.exceptions.EventNotFound;
import ru.misis.event.exceptions.EventValidation;
import ru.misis.skill.exceptions.SkillNotFound;
import ru.misis.skill.exceptions.SkillValidation;
import ru.misis.user.exceptions.UserAlreadyExists;
import ru.misis.user.exceptions.UserNotFound;
import ru.misis.user.exceptions.UserValidation;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError springValidationHandler(final MethodArgumentNotValidException e) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.name(),
                e.getBindingResult().getFieldErrors().get(0).getDefaultMessage()
        );
    }

    @ExceptionHandler({
            UserAlreadyExists.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError conflictHandler(final Exception e) {
        return new ApiError(
                HttpStatus.CONFLICT.name(),
                e.getMessage()
        );
    }

    @ExceptionHandler({
            UserNotFound.class,
            SkillNotFound.class,
            CategoryNotFound.class,
            EventNotFound.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError entityNotFoundHandler(final Exception e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.name(),
                e.getMessage()
        );
    }

    @ExceptionHandler({
            SkillValidation.class,
            CategoryValidation.class,
            UserValidation.class,
            EventValidation.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError badRequestHandler(final Exception e) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.name(),
                e.getMessage()
        );
    }

    @ExceptionHandler({
            AuthFailed.class
    })
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiError serviceUnavailableHandler(final Exception e) {
        return new ApiError(
                HttpStatus.SERVICE_UNAVAILABLE.name(),
                e.getMessage()
        );
    }

    @ExceptionHandler({
            Forbidden.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError forbiddenHandler(final Exception e) {
        return new ApiError(
                HttpStatus.FORBIDDEN.name(),
                e.getMessage()
        );
    }
}
