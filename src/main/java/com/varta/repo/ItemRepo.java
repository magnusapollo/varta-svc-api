package com.varta.repo;

import com.varta.domain.Item;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ItemRepo {
    private final Map<String, Item> byId = new ConcurrentHashMap<>();
    private final Map<String, String> bySlug = new ConcurrentHashMap<>();
    private final Map<String, String> byUrlHash = new ConcurrentHashMap<>();

    public Collection<Item> all() {
        return byId.values();
    }

    public Optional<Item> findBySlug(String slug) {
        return Optional.ofNullable(byId.get(bySlug.get(slug)));
    }

    public Optional<Item> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public void upsert(String urlHash, Item item) {
        byId.put(item.id, item);
        bySlug.put(item.slug, item.id);
        byUrlHash.put(urlHash, item.id);
    }

    public Optional<Item> findByUrlHash(String urlHash) {
        var itemId = byUrlHash.get(urlHash);
        return itemId == null ? Optional.empty() : Optional.ofNullable(byId.get(itemId));
    }

    public long count() {
        return byId.size();
    }
}