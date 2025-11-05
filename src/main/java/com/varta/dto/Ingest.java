package com.varta.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Ingest {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Source(String name, String type, String baseUrl) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record IngestItem(
            String url,
            String title,
            String author,
            Instant publishedAt,
            String rawText,
            String excerpt,
            String canonicalUrl,
            String slug) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record IngestEnrichment(
            String url,
            String summaryBullets,
            String whyItMatters,
            Map<String, List<String>> entities,
            List<String> topics) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record IngestMetric(String url, Double rankScore, Integer socialScore) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BulkUpsertPayload(Source source, List<IngestItem> items,
                                    List<IngestEnrichment> enrichments, List<IngestMetric> metrics) {
    }
}