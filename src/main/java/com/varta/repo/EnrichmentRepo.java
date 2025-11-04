package com.varta.repo;

import com.varta.domain.Enrichment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EnrichmentRepo {
    private final Map<String, Enrichment> byItemId = new ConcurrentHashMap<>();
    public Optional<Enrichment> forItem(String itemId) { return Optional.ofNullable(byItemId.get(itemId)); }
    public void put(Enrichment e) { byItemId.put(e.itemId, e); }
}