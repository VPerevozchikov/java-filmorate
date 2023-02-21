package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
public class Film extends AbstractModel {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private int duration;

    public Set<Long> listOfLikes = new HashSet<>();

    public Film(String name, String description, String releaseDate, int duration) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.name = name;
        this.description = description;
        this.releaseDate = LocalDate.parse(releaseDate, formatter);
        this.duration = duration;
    }
}