package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractModel;

import java.util.HashMap;
import java.util.Map;

abstract public class AbstractController<T extends AbstractModel> {
    Map<Integer, T> data = new HashMap<>();
    Integer counter = 0;

    public T createItem(T item) throws ValidationException {
        validate(item);
        counter++;
        item.setId(counter);
        data.put(item.getId(), item);
        return item;
    }

    public T updateItem(T item) throws ValidationException {
        validate(item);
        if (data.containsKey(item.getId())) {
            data.put(item.getId(), item);
            return item;
        } else {
            throw new ValidationException("Ошибка в ID");
        }
    }

    abstract void validate(T item) throws ValidationException;
}