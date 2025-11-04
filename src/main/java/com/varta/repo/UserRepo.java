package com.varta.repo;

import com.varta.domain.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserRepo {
    private final Map<String, User> byId = new ConcurrentHashMap<>();
    private final Map<String, String> byEmail = new ConcurrentHashMap<>();

    public void put(User u) {
        byId.put(u.id, u);
        byEmail.put(u.email, u.id);
    }

    public Optional<User> byId(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public Optional<User> byEmail(String email) {
        return Optional.ofNullable(byId.get(byEmail.get(email)));
    }
}