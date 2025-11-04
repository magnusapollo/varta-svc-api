package com.varta.repo;

import com.varta.domain.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PostRepo {
    private final Map<String, Post> byId = new ConcurrentHashMap<>();
    private final Map<String, String> bySlug = new ConcurrentHashMap<>();

    public void put(Post p) {
        byId.put(p.id, p);
        bySlug.put(p.slug, p.id);
    }

    public Optional<Post> bySlug(String slug) {
        return Optional.ofNullable(byId.get(bySlug.get(slug)));
    }

    public List<Post> all() {
        return new ArrayList<>(byId.values());
    }
}