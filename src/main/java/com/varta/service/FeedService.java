package com.varta.service;

import com.varta.domain.Enrichment;
import com.varta.domain.Item;
import com.varta.domain.Metrics;
import com.varta.dto.FeedItem;
import com.varta.repo.EnrichmentRepo;
import com.varta.repo.ItemRepo;
import com.varta.repo.MetricsRepo;
import com.varta.util.PaginationUtil;
import com.varta.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("feedService")
@RequiredArgsConstructor
public class FeedService {
    private final ItemRepo items;
    private final EnrichmentRepo enrich;
    private final MetricsRepo metrics;

    public record PageResult(List<FeedItem> items, long total) {
    }

    public PageResult feed(String topic, String sort, String since, int page, int pageSize) {
        Instant cutoff = TimeUtil.parseSince(since);
        List<Item> base = items.all().stream()
                .filter(i -> "published".equalsIgnoreCase(i.status))
                .filter(i -> i.publishedAt != null && i.publishedAt.isAfter(cutoff))
                .filter(i -> topic == null || topic.isBlank() || (i.topics != null && i.topics.contains(topic)))
                .collect(Collectors.toList());

        Comparator<Item> cmpLatest = Comparator.comparing((Item i) -> i.publishedAt).reversed();
        Comparator<Item> cmpTop = Comparator
                .comparingDouble((Item i) -> metrics.forItem(i.id).map(m -> m.rankScore).orElse(0.0))
                .reversed()
                .thenComparing((Item i) -> i.publishedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed();

        base.sort("top".equalsIgnoreCase(sort) ? cmpTop : cmpLatest);

        long total = base.size();
        List<Item> slice = PaginationUtil.page(base, page, pageSize);

        List<FeedItem> out = slice.stream().map(this::toFeedItem).toList();
        return new PageResult(out, total);
    }

    protected FeedItem toFeedItem(Item i) {
        Enrichment e = enrich.forItem(i.id).orElse(null);
        Metrics m = metrics.forItem(i.id).orElse(null);
        return new FeedItem(
                i.id, i.slug, i.title, i.excerpt, i.url,
                new FeedItem.SourceDto(i.source.id(), i.source.name(), i.source.type(), i.source.domain()),
                i.publishedAt, i.topics,
                e != null ? e.summaryBullets : null,
                e != null ? e.whyItMatters : null,
                m != null ? m.rankScore : null);
    }
}