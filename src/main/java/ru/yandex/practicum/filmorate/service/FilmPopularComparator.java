package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

@Service
public class FilmPopularComparator implements Comparator<Film> {

    @Override
    public int compare(Film f1, Film f2) {
        if (f1.listOfLikes.size() < f2.listOfLikes.size()) {
            return 1;

        } else if (f1.listOfLikes.size() > f2.listOfLikes.size()) {
            return -1;

        } else {
            return 0;
        }
    }
}