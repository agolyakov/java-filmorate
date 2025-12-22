package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"login"})
@Builder
public class User {
    @Builder.Default
    Long id = 0L;
    @NotBlank
    @Email
    String email;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$")
    String login;
    String name;
    @PastOrPresent
    LocalDate birthday;
}
