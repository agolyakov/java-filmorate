package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.MinReleaseDate;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@Builder
public class Film {
    @Builder.Default
    Long id = 0L;
    @NotBlank(message = "Название не может быть пустым")
    String name;
    @Size(max = 200, message = "Длина описания не должна превышать 200 символов")
    String description;
    @MinReleaseDate
    LocalDate releaseDate;
    @Positive
    Long duration;
}
