package com.varta.service;

import com.varta.domain.User;
import com.varta.repo.UserRepo;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepo users;
    public AuthService(UserRepo users) { this.users = users; }

    public record Flow(String flowId, String email) {}

    public Flow start(String email) { return new Flow(UUID.randomUUID().toString(), email); }

    public User verify(String flowId, String code, String email) {
// Stub: accept anything, create/get user
        Optional<User> u = users.byEmail(email);
        if (u.isPresent()) return u.get();
        User nu = new User();
        nu.id = UUID.randomUUID().toString();
        nu.email = email;
        users.put(nu);
        return nu;
    }
}