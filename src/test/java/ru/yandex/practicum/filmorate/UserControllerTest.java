package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    TestRestTemplate template;

    @Test
    void shouldReturnAllUsers() {
        ResponseEntity<User[]> entity = template.getForEntity("/users", User[].class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());
    }

    @Test
    void shouldCreateNewUserWithEmptyEmail() {
        User user = User.builder()
                .login("login")
                .birthday(LocalDate.now())
                .build();

        ResponseEntity<User> entity = template.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewUserWithEmailWithoutAt() {
        User user = User.builder()
                .email("hello.com")
                .login("login")
                .birthday(LocalDate.now())
                .build();

        ResponseEntity<User> entity = template.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewUserWithEmail() {
        User user = User.builder()
                .email("test@hello.com")
                .login("login")
                .birthday(LocalDate.now())
                .build();

        ResponseEntity<User> entity = template.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewUserWithEmptyLogin() {
        User user = User.builder()
                .email("test@hello.com")
                .birthday(LocalDate.now())
                .build();

        ResponseEntity<User> entity = template.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewUserWithLoginAtBlank() {
        User user = User.builder()
                .email("test@hello.com")
                .login(" ")
                .birthday(LocalDate.now())
                .build();

        ResponseEntity<User> entity = template.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewUserWithLogin() {
        User user = User.builder()
                .email("test@hello.com")
                .login("login")
                .birthday(LocalDate.now())
                .build();

        ResponseEntity<User> entity = template.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewUserWithEmptyName() {
        User user = User.builder()
                .email("test@hello.com")
                .login("login")
                .birthday(LocalDate.now())
                .build();

        ResponseEntity<User> entity = template.postForEntity("/users", user, User.class);
        User created = entity.getBody();
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
        Assertions.assertEquals("login", created.getName());
    }

    @Test
    void shouldCreateNewUserWithName() {
        User user = User.builder()
                .email("test@hello.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        ResponseEntity<User> entity = template.postForEntity("/users", user, User.class);
        User created = entity.getBody();
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
        Assertions.assertEquals("name", created.getName());
    }

    @Test
    void shouldCreateNewUserWithBirthdayIsFuture() {
        User user = User.builder()
                .email("test@hello.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2025,12,22))
                .build();

        ResponseEntity<User> entity = template.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewUserWithBirthdayIsNow() {
        User user = User.builder()
                .email("test@hello.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.now())
                .build();

        ResponseEntity<User> entity = template.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    @Test
    void shouldCreateNewUserWithBirthdayIsPast() {
        User user = User.builder()
                .email("test@hello.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2025,12,20))
                .build();

        ResponseEntity<User> entity = template.postForEntity("/users", user, User.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
    }
}