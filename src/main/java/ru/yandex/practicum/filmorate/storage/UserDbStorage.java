package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into users(EMAIL, LOGIN, NAME, BIRTHDAY)" +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where EMAIL = ?", user.getEmail());
        User newUser = new User();
        if (userRows.next()) {
            newUser.setId(userRows.getLong("id"));
            newUser.setEmail(userRows.getString("email"));
            newUser.setLogin(userRows.getString("login"));
            newUser.setName(userRows.getString("name"));
            newUser.setBirthday(LocalDate.parse(Objects.requireNonNull(userRows.getString("birthday")), formatter));
        }
        return newUser;
    }

    @Override
    public boolean deleteUserById(Long id) {
        String sqlQuery = "delete from users where id = ?";
        if (jdbcTemplate.update(sqlQuery, id) > 0) {
            return true;
        } else {
            throw new UserNotFoundException(String.format(
                    "Пользователь c id %s не найден", id));
        }
    }

    @Override
    public User updateUser(User user) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", user.getId());
        if (!userRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Пользователь c id %s не найден", user.getId()));
        }
        String sqlQuery = "update users set " +
                "EMAIL =?, LOGIN = ?, NAME = ?, BIRTHDAY = ?" +
                "where id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public User getUserById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        User user = new User();
        if (userRows.next()) {
            user.setId(userRows.getLong("id"));
            user.setEmail(userRows.getString("email"));
            user.setLogin(userRows.getString("login"));
            user.setName(userRows.getString("name"));
            user.setBirthday(LocalDate.parse(Objects.requireNonNull(userRows.getString("birthday")), formatter));
        } else {
            throw new UserNotFoundException(String.format(
                    "Пользователь c id %s не найден", id));
        }
        return user;
    }

    @Override
    public void addFriend(Long id1, Long id2) {
        SqlRowSet usersOneRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id1);
        SqlRowSet usersTwoRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id2);
        if (!usersOneRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Пользователь c id %s не найден", id1));
        } else if (!usersTwoRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Пользователь c id %s не найден", id2));
        }
        String sqlQuery = "insert into list_of_friends(USER_1_ID, USER_2_ID)" +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                id1,
                id2);
    }

    @Override
    public void deleteFriend(Long id1, Long id2) {
        SqlRowSet usersOneRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id1);
        SqlRowSet usersTwoRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id2);
        if (!usersOneRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Пользователь c id %s не найден", id1));
        } else if (!usersTwoRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Пользователь c id %s не найден", id2));
        }
        String sqlQuery = "delete from list_of_friends where USER_1_ID = ? AND USER_2_ID = ?";
        jdbcTemplate.update(sqlQuery, id1, id2);
    }

    @Override
    public List<User> getUsers() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public List<User> getListOfFriends(Long id) {
        String sql = "SELECT id, email, login, name, birthday " +
                "FROM USERS " +
                "WHERE ID IN (" +
                "SELECT USER_2_ID " +
                "FROM LIST_OF_FRIENDS " +
                "WHERE USER_1_ID = ?" +
                ")";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public List<User> getListOfMutualFriends(Long id1, Long id2) {
        String sql = "SELECT id, email, login, name, birthday " +
                "FROM USERS " +
                "JOIN (" +
                "SELECT USER_2_ID," +
                "   COUNT(USER_2_ID)" +
                " FROM LIST_OF_FRIENDS " +
                "WHERE USER_1_ID = ? " +
                "OR USER_1_ID = ? " +
                "GROUP BY USER_2_ID " +
                "HAVING COUNT(USER_2_ID) =2) j ON id = j.user_2_ID;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id1, id2);

    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId((long) rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(LocalDate.parse(Objects.requireNonNull(rs.getString("birthday")), formatter));
        return user;
    }
}