package com.varta.service;

import com.varta.domain.*;
import com.varta.dto.ItemDetail;
import com.varta.repo.*;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private final ItemRepo items; private final EnrichmentRepo enrich; private final MetricsRepo metrics;
    public ItemService(ItemRepo items, EnrichmentRepo enrich, MetricsRepo metrics) {
        this.items = items; this.enrich = enrich; this.metrics = metrics;
    }

    public Optional<ItemDetail> bySlug(String slug) {
        return items.findBySlug(slug).map(i -> {
            Enrichment e = enrich.forItem(i.id).orElse(null);
            Metrics m = metrics.forItem(i.id).orElse(null);
            return new ItemDetail(
                    new ItemDetail.Item(i.id, i.slug, i.title, i.author, i.url, i.publishedAt, i.canonicalUrl,
                            new ItemDetail.Source(i.source.id(), i.source.name(), i.source.type(), i.source.domain())),
                    e == null ? null : new ItemDetail.Enrichment(e.summaryBullets, e.whyItMatters, e.entities, e.topics),
                    m == null ? new ItemDetail.Metrics(null, null, null, null)
                            : new ItemDetail.Metrics(m.rankScore, m.socialScore, m.hnPoints, m.ghStars));
        });
    }
}