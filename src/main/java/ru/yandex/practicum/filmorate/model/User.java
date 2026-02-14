package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"login"})
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @__(@Builder))
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
    @Builder.Default
    Set<Long> friends = new HashSet<>();
    @Builder.Default
    Set<Long> likes = new HashSet<>();

}
