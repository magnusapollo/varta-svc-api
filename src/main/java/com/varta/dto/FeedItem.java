package com.varta.dto;

import java.time.Instant;
import java.util.List;

public record FeedItem(
        String id,
        String slug,
        String title,
        String excerpt,
        String url,
        SourceDto source,
        Instant publishedAt,
        List<String> topics,
        String summaryBullets,
        String whyItMatters,
        Double rankScore) {

    public record SourceDto(String id, String name, String type, String domain) {}
}