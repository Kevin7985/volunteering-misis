package ru.misis.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.misis.auth.exceptions.AuthFailed;
import ru.misis.error.model.ApiError;
import ru.misis.user.exceptions.UserAlreadyExists;
import ru.misis.user.exceptions.UserNotFound;

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
            UserNotFound.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError entityNotFoundHandler(final Exception e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.name(),
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
}
