package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmControllerTest {
    @Autowired
    TestRestTemplate template;

    @Test
    void shouldReturnAllFilms() {
        ResponseEntity<Film[]> entity = template.getForEntity("/films", Film[].class);

        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());
    }

    @Test
    void shouldCreateNewFilmNameEmpty() {
        Film film = Film.builder()
                .name("")
                .build();

        ResponseEntity<Film> entity = template.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewFilmNameBlank() {
        Film film = Film.builder()
                .name(" ")
                .build();

        ResponseEntity<Film> entity = template.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewFilmNameNull() {
        Film film = Film.builder()
                .build();

        ResponseEntity<Film> entity = template.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewFilmDescriptionOverMaxSymbols() {
        // 201 символ
        Film film201 = Film.builder()
                .name("Name")
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                        "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, " +
                        "а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.")
                .build();

        ResponseEntity<Film> entity = template.postForEntity("/films", film201, Film.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewFilmDescriptionMaxSymbol() {
        // 200 символов
        Film film200 = Film.builder()
                .name("Name")
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                        "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, " +
                        "а именно 20 миллионов. о Куглов, который за время «сво")
                .build();

        ResponseEntity<Film> entity = template.postForEntity("/films", film200, Film.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewFilmReleaseDateBeforeMin() {
        Film film = Film.builder()
                .name("Name")
                .description("Desc")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .build();

        ResponseEntity<Film> entity = template.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewFilmReleaseDate() {
        Film film = Film.builder()
                .name("Name")
                .description("Desc")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build();

        ResponseEntity<Film> entity = template.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewFilmReleaseDateAfterMin() {
        Film film = Film.builder()
                .name("Name")
                .description("Desc")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .build();

        ResponseEntity<Film> entity = template.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewFilmDurationPositive() {
        Film film = Film.builder()
                .name("Name")
                .description("Desc")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(1L)
                .build();

        ResponseEntity<Film> entity = template.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewFilmDurationZero() {
        Film film = Film.builder()
                .name("Name")
                .description("Desc")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(0L)
                .build();

        ResponseEntity<Film> entity = template.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewFilmDurationNegative() {
        Film film = Film.builder()
                .name("Name")
                .description("Desc")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(-1L)
                .build();

        ResponseEntity<Film> entity = template.postForEntity("/films", film, Film.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }
}
