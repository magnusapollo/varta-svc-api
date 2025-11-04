package com.varta.repo;

import com.varta.domain.Metrics;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MetricsRepo {
    private final Map<String, Metrics> byItemId = new ConcurrentHashMap<>();

    public Optional<Metrics> forItem(String itemId) {
        return Optional.ofNullable(byItemId.get(itemId));
    }

    public void put(Metrics m) {
        byItemId.put(m.itemId, m);
    }
}