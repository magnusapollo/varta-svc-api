package com.varta.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ItemDetail(Item item, Enrichment enrichment, Metrics metrics) {
    public record Item(
            String id,
            String slug,
            String title,
            String author,
            String url,
            Instant publishedAt,
            String canonicalUrl,
            Source source) {}

    public record Source(String id, String name, String type, String domain) {}

    public record Enrichment(
            String summaryBullets,
            String whyItMatters,
            Map<String, List<String>> entities,
            List<String> topics) {}

    public record Metrics(Double rankScore, Integer socialScore, Integer hnPoints, Integer ghStars) {}
}