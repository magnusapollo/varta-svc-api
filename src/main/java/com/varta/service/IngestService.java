package com.varta.service;


import com.varta.domain.Enrichment;
import com.varta.domain.Item;
import com.varta.domain.Metrics;
import com.varta.domain.SourceRef;
import com.varta.dto.Ingest.BulkUpsertPayload;
import com.varta.dto.Ingest.IngestEnrichment;
import com.varta.dto.Ingest.IngestItem;
import com.varta.dto.Ingest.IngestMetric;
import com.varta.repo.EnrichmentRepo;
import com.varta.repo.ItemRepo;
import com.varta.repo.MetricsRepo;
import com.varta.util.Hashing;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IngestService {
    private final ItemRepo items;
    private final EnrichmentRepo enrich;
    private final MetricsRepo metrics;

    public IngestService(ItemRepo items, EnrichmentRepo enrich, MetricsRepo metrics) {
        this.items = items;
        this.enrich = enrich;
        this.metrics = metrics;
    }

    public record Result(int itemsUpserted, int enrichmentsUpserted, int metricsUpserted) {
    }

    public Result bulkUpsert(BulkUpsertPayload payload) {
        Map<String, IngestItem> byUrl = payload.items() == null ? Map.of() : payload.items().stream().collect(Collectors.toMap(IngestItem::url, it -> it, (a, b) -> a));
        Map<String, IngestEnrichment> eByUrl = payload.enrichments() == null ? Map.of() : payload.enrichments().stream().collect(Collectors.toMap(IngestEnrichment::url, it -> it, (a, b) -> a));
        Map<String, IngestMetric> mByUrl = payload.metrics() == null ? Map.of() : payload.metrics().stream().collect(Collectors.toMap(IngestMetric::url, it -> it, (a, b) -> a));

        int ic = 0, ec = 0, mc = 0;
        for (var entry : byUrl.entrySet()) {
            String url = entry.getKey();
            IngestItem in = entry.getValue();
            String urlHash = Hashing.sha256(in.canonicalUrl() != null ? in.canonicalUrl() : url);
            var existing = items.findByUrlHash(urlHash);

            Item item = existing.orElseGet(Item::new);
            boolean newer = existing.isEmpty() || (item.publishedAt == null || in.publishedAt().isAfter(item.publishedAt));
            if (newer) {
                if (item.id == null) item.id = UUID.randomUUID().toString();
                item.slug = (in.slug() != null && !in.slug().isBlank()) ? in.slug() : slugify(in.title());
                item.title = in.title();
                item.author = in.author();
                item.url = url;
                item.canonicalUrl = in.canonicalUrl() != null ? in.canonicalUrl() : url;
                item.source = new SourceRef(UUID.randomUUID().toString(), payload.source().name(), payload.source().type(), host(url));
                item.publishedAt = in.publishedAt() != null ? in.publishedAt() : Instant.now();
                item.excerpt = in.excerpt();
                item.status = "published";
                item.topics = List.of("app-dev");
                items.upsert(urlHash, item);
                ic++;
            }

            IngestEnrichment en = eByUrl.get(url);
            if (en != null) {
                Enrichment e = new Enrichment();
                e.itemId = item.id;
                e.summaryBullets = en.summaryBullets();
                e.whyItMatters = en.whyItMatters();
                e.entities = en.entities();
                e.topics = en.topics();
                enrich.put(e);
                ec++;
            }

            IngestMetric mm = mByUrl.get(url);
            if (mm != null) {
                Metrics m = new Metrics();
                m.itemId = item.id;
                m.rankScore = mm.rankScore() == null ? 0.0 : mm.rankScore();
                m.socialScore = mm.socialScore();
                m.hnPoints = 0;
                m.ghStars = 0;
                metrics.put(m);
                mc++;
            }
        }

        return new Result(ic, ec, mc);
    }

    private static String slugify(String s) {
        return s.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }

    private static String host(String url) {
        try {
            String h = new java.net.URI(url).getHost();
            return h == null ? "" : (h.startsWith("www.") ? h.substring(4) : h);
        } catch (Exception e) {
            return "";
        }
    }
}