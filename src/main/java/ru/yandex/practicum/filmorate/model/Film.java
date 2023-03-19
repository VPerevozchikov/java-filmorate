package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Film extends AbstractModel {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @Size(min = 1, max = 200)
    private String description;
    @Min(1)
    private int duration;
    @NotNull
    private LocalDate releaseDate;
    private Mpa Mpa;
    private List<Genre> genres;
}