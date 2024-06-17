package ru.practicum.shareit.exceptions;

public class WrongItemOwnerException extends RuntimeException {
    public WrongItemOwnerException(String message) {
        super(message);
    }
}
