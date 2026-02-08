package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.MinReleaseDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @__(@Builder))
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
    @Builder.Default
    Set<Long> likes = new HashSet<>();
}
