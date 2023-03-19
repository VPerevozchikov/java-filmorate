package ru.yandex.practicum.filmorate.exceptions;

public class RatingMpaNotFoundException extends RuntimeException {
    public RatingMpaNotFoundException(String message) {
        super(message);
    }
}
