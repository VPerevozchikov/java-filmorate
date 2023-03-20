package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into films(NAME, DESCRIPTION, DURATION, " +
                "RELEASE_DATE, MPA_ID) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId());

        SqlRowSet filmRowsForReturnFilm = jdbcTemplate.queryForRowSet("select * " +
                "from films join mpa on MPA_ID = MPA_ORIG_ID" +
                " where NAME = ?", film.getName());

        Film newFilm = makeFilmFromSrs(filmRowsForReturnFilm);

        if (film.getGenres() != null) {
            String sql = "insert into LIST_OF_GENRES (FILM_ID, GENRE_ID)" +
                    "values (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sql,
                        newFilm.getId(),
                        genre.getId());
            }
        }

        String sqlQueryGenres = "select DISTINCT genre.ID, genre.NAME, LIST_OF_GENRES.FILM_ID " +
                "from genre " +
                "join LIST_OF_GENRES on genre.ID = LIST_OF_GENRES.genre_ID " +
                "WHERE LIST_OF_GENRES.FILM_ID = ? " +
                "ORDER BY GENRE.ID";
        newFilm.setGenres(new ArrayList<>(jdbcTemplate.query(sqlQueryGenres, (rs, rowNum) -> makeGenre(rs),
                newFilm.getId())));

        return newFilm;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "select * from films join mpa on films.MPA_ID = mpa.MPA_ORIG_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmsFromRs(rs));
    }

    @Override
    public boolean deleteFilmById(Long id) {
        String sqlQuery = "delete from films where id = ?";
        if (jdbcTemplate.update(sqlQuery, id) > 0) {
            return true;
        } else {
            throw new FilmNotFoundException(String.format(
                    "Фильм c id %s не найден", id));
        }
    }

    @Override
    public Film updateFilm(Film film) {

        SqlRowSet filmRowsPre = jdbcTemplate.queryForRowSet("select * from films " +
                "where id = ?", film.getId());
        if (!filmRowsPre.next()) {
            throw new FilmNotFoundException(String.format(
                    "Фильм c id %s не найден", film.getId()));
        }

        String sqlQuery = "update films set " +
                "NAME =?, DESCRIPTION = ?, DURATION = ?," +
                "RELEASE_DATE = ?, MPA_ID = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());

        String sqlQueryDeleteGenres = "DELETE FROM LIST_OF_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDeleteGenres, film.getId());

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * " +
                "from films join mpa on MPA_ID = MPA_ORIG_ID" +
                " where NAME = ?", film.getName());
        Film newFilm = makeFilmFromSrs(filmRows);
        if (film.getGenres() != null) {
            String sql = "insert into LIST_OF_GENRES (FILM_ID, GENRE_ID)" +
                    "values (?, ?)";

            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sql,
                        newFilm.getId(),
                        genre.getId());
            }

            String sqlQueryGenres = "select DISTINCT genre.ID, genre.NAME, LIST_OF_GENRES.FILM_ID " +
                    "from genre " +
                    "join LIST_OF_GENRES on genre.ID = LIST_OF_GENRES.genre_ID " +
                    "WHERE LIST_OF_GENRES.FILM_ID = ?" +
                    "ORDER BY GENRE.ID ";
            newFilm.setGenres(new ArrayList<>(jdbcTemplate.query(sqlQueryGenres, (rs, rowNum) -> makeGenre(rs),
                    newFilm.getId())));
        }
        return newFilm;
    }

    @Override
    public Film getFilmById(Long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * " +
                "from FILMS join MPA on films.MPA_ID = mpa.MPA_ORIG_ID " +
                "where ID = ?", id);
        return makeFilmFromSrs(filmRows);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", filmId);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", userId);
        if (!filmRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Фильм c id %s не найден", filmId));
        } else if (!userRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Пользователь c id %s не найден", userId));
        }
        String sqlQuery = "insert into list_of_likes(FILM_ID, USER_ID)" +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                filmId,
                userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", filmId);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", userId);
        if (!filmRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Фильм c id %s не найден", filmId));
        } else if (!userRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Пользователь c id %s не найден", userId));
        }
        String sqlQuery = "delete from list_of_likes where FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void addGenre(Long filmId, Long genreId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", filmId);
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genre where id = ?", genreId);
        if (!filmRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Фильм c id %s не найден", filmId));
        } else if (!genreRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Жанр c id %s не найден", genreId));
        }
        String sqlQuery = "insert into list_of_genres(FILM_ID, GENRE_ID)" +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                filmId,
                genreId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {


        String sqlOne = "SELECT FILMS.ID, NAME, DESCRIPTION, DURATION, RELEASE_DATE, MPA_ID, MPA_NAME, " +
                "COUNT(LIST_OF_LIKES.film_id) " +
                "FROM FILMS " +
                "LEFT JOIN LIST_OF_LIKES ON FILMS.id=LIST_OF_LIKES.film_id " +
                "join mpa on FILMS.MPA_ID = mpa.MPA_ORIG_ID " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(LIST_OF_LIKES.film_id) DESC " +
                "LIMIT ?";


        return jdbcTemplate.query(sqlOne, (rs, rowNum) -> makeFilmsFromRs(rs), count);
    }

    @Override
    public Genre getGenreById(Long id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRE where id = ?", id);
        if (!genreRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Жанр c id %s не найден", id));
        }
        Genre genre = new Genre();
        genre.setId(genreRows.getLong("id"));
        genre.setName(genreRows.getString("name"));

        return genre;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "select * from genre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Mpa getMpaById(Long id) {
        SqlRowSet ratingMpaRows = jdbcTemplate.queryForRowSet("select * from MPA where MPA_ORIG_ID = ?", id);
        if (!ratingMpaRows.next()) {
            throw new FilmNotFoundException(String.format(
                    "Рейтинг MPA c id %s не найден", id));
        }
        Mpa mpa = new Mpa();
        mpa.setId(ratingMpaRows.getLong("mpa_orig_id"));
        mpa.setName(ratingMpaRows.getString("mpa_name"));
        return mpa;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "select * from MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    private Film makeFilmsFromRs(ResultSet rs) throws SQLException {
        Film film = new Film();
        Mpa mpa = new Mpa();

        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));
        film.setReleaseDate(LocalDate.parse(Objects.requireNonNull(rs.getString("release_Date")),
                formatter));

        mpa.setId((long) rs.getInt("mpa_id"));
        mpa.setName(rs.getString("mpa_name"));

        film.setMpa(mpa);

        String sqlQueryGenres = "select DISTINCT genre.ID, genre.NAME, LIST_OF_GENRES.FILM_ID  " +
                "from genre join LIST_OF_GENRES on genre.ID = LIST_OF_GENRES.genre_ID " +
                "where FILM_ID=?" +
                "ORDER BY GENRE.ID";
        film.setGenres(new ArrayList<>(jdbcTemplate.query(sqlQueryGenres, (rsTwo, rowNum) -> makeGenre(rsTwo),
                film.getId())));

        return film;
    }

    private Film makeFilmFromSrs(SqlRowSet srs) {
        Film film = new Film();
        Mpa mpa = new Mpa();
        if (srs.next()) {
            film.setId(srs.getLong("ID"));
            film.setName(srs.getString("NAME"));
            film.setDescription(srs.getString("DESCRIPTION"));
            film.setDuration(srs.getInt("DURATION"));
            film.setReleaseDate(LocalDate.parse(Objects.requireNonNull(srs.getString("RELEASE_DATE")),
                    formatter));

            mpa.setId((long) srs.getInt("MPA_ID"));
            mpa.setName(srs.getString("MPA_NAME"));

            film.setMpa(mpa);

            String sqlQueryGenres = "select DISTINCT genre.ID, genre.NAME, LIST_OF_GENRES.FILM_ID from genre join LIST_OF_GENRES on genre.ID = LIST_OF_GENRES.genre_ID " +
                    "where LIST_OF_GENRES.FILM_ID = ?" +
                    "ORDER BY GENRE.ID";
            film.setGenres(new ArrayList<>(jdbcTemplate.query(sqlQueryGenres, (rs, rowNum) -> makeGenre(rs),
                    film.getId())));
        } else {
            throw new FilmNotFoundException(String.format("Фильм не найден"));
        }
        return film;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getLong("id"));
        genre.setName(rs.getString("name"));
        return genre;
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("mpa_orig_id"));
        mpa.setName(rs.getString("mpa_name"));
        return mpa;
    }
}