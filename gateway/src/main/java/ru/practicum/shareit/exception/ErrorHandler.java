package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.response.ErrorResponse;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(basePackages = "ru.practicum.shareit")
public class ErrorHandler {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(Exception e) {
        return new ErrorResponse("Неверно составлен запрос!", e.getMessage());
    }
}
