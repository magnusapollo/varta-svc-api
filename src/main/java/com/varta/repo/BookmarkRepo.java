package com.varta.repo;


import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BookmarkRepo {
    // userId -> set(itemId)
    private final Map<String, Set<String>> userBookmarks = new ConcurrentHashMap<>();

    public Set<String> list(String userId) {
        return userBookmarks.getOrDefault(userId, new HashSet<>());
    }

    public void add(String userId, String itemId) {
        userBookmarks.computeIfAbsent(userId, k -> new HashSet<>()).add(itemId);
    }

    public void remove(String userId, String itemId) {
        userBookmarks.computeIfAbsent(userId, k -> new HashSet<>()).remove(itemId);
    }
}