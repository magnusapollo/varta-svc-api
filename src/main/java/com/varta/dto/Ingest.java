package com.varta.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Ingest {
    public record Source(String name, String type, String baseUrl) {}

    public record IngestItem(
            String url,
            String title,
            String author,
            Instant publishedAt,
            String rawText,
            String excerpt,
            String canonicalUrl,
            String slug) {}

    public record IngestEnrichment(
            String url,
            String summaryBullets,
            String whyItMatters,
            Map<String, List<String>> entities,
            List<String> topics) {}

    public record IngestMetric(String url, Double rankScore, Integer socialScore) {}

    public record BulkUpsertPayload(Source source, List<IngestItem> items,
                                    List<IngestEnrichment> enrichments, List<IngestMetric> metrics) {}
}