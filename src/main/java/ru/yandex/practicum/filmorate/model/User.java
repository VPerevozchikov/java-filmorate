package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
public class User extends AbstractModel {

    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @NotNull
    private LocalDate birthday;

    public Set<Long> listOfFriends = new HashSet<>();

    public User(String email, String login, String name, String birthday) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = LocalDate.parse(birthday, formatter);
    }
}