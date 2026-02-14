package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final InMemoryUserStorage inMemoryUserStorage;

    public User addFriend(long userId, long friendId) {
        User user = inMemoryUserStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        User friend = inMemoryUserStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + friendId + " не найден"));

        addToFriends(user, friendId);
        addToFriends(friend, userId);

        inMemoryUserStorage.update(friend);
        return inMemoryUserStorage.update(user);
    }

    public User removeFriend(long userId, long friendId) {
        User user = inMemoryUserStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        User friend = inMemoryUserStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + friendId + " не найден"));

        removeFromFriends(user, friendId);
        removeFromFriends(friend, userId);

        inMemoryUserStorage.update(friend);
        return inMemoryUserStorage.update(user);
    }

    public Collection<User> findAllOurFriends(long userId, long otherUserId) {
        User user = inMemoryUserStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        User otherUser = inMemoryUserStorage.findById(otherUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + otherUserId + " не найден"));


        Set<Long> userFriends  = user.getFriends();
        Set<Long> otherUserFriends = otherUser.getFriends();

        Set<Long> intersection = new HashSet<>(userFriends);
        intersection.retainAll(otherUserFriends);

        return intersection.stream()
                .map(id -> inMemoryUserStorage.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    public User update(User user) {
        return inMemoryUserStorage.update(user);
    }

    public Collection<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public Collection<User> findAllFriends(long userId) {
        return inMemoryUserStorage.findAllFriends(userId);
    }

    private void addToFriends(User user, long friendId) {
        user.getFriends().add(friendId);
    }

    private void removeFromFriends(User user, long friendId) {
        Set<Long> friends = user.getFriends();
        friends.remove(friendId);
        user.setFriends(friends);
    }
}
