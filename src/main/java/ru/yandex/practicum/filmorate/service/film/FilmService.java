package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmService {

    @Autowired
    private final InMemoryFilmStorage inMemoryFilmStorage;
    @Autowired
    private final InMemoryUserStorage inMemoryUserStorage;

    public Film addLike(long filmId, long userId) {
        Film film = inMemoryFilmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));
        User user = inMemoryUserStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        addLikes(film, user);

        inMemoryUserStorage.update(user);
        return inMemoryFilmStorage.update(film);
    }

    public Film removeLike(long filmId, long userId) {
        Film film = inMemoryFilmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));
        User user = inMemoryUserStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        removeLikes(film, user);

        inMemoryUserStorage.update(user);
        return inMemoryFilmStorage.update(film);
    }

    public Collection<Film> topPopulateFilm(long count) {
        return inMemoryFilmStorage.findAll().stream()
                .filter(film -> film.getLikes() != null)
                .sorted((f1, f2) -> Integer.compare(
                        f2.getLikes().size(),
                        f1.getLikes().size()))
                .limit(count)
                .toList();
    }

    private void addLikes(Film film, User user) {
        film.getLikes().add(user.getId());
        user.getLikes().add(film.getId());
    }

    private void removeLikes(Film film, User user) {
        Set<Long> filmLikes = film.getLikes();
        Set<Long> userLikes = user.getLikes();

        filmLikes.remove(user.getId());
        userLikes.remove(film.getId());

        film.setLikes(filmLikes);
        user.setLikes(userLikes);
    }
}
