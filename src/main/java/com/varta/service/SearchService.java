package com.varta.service;


import com.varta.domain.Item;
import com.varta.dto.FeedItem;
import com.varta.repo.EnrichmentRepo;
import com.varta.repo.ItemRepo;
import com.varta.repo.MetricsRepo;
import com.varta.util.TimeUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService extends FeedService {
    private final ItemRepo items;

    public SearchService(ItemRepo items, EnrichmentRepo e, MetricsRepo m) {
        super(items, e, m);
        this.items = items;
    }

    public record PageResult(List<FeedItem> results, long total) {
    }

    public PageResult search(String q, String topic, String since, int page, int pageSize) {
        Instant cutoff = TimeUtil.parseSince(since);
        String[] tokens = (q == null ? "" : q.toLowerCase()).split("\\s+");
        Set<String> tset = Arrays.stream(tokens).filter(s -> !s.isBlank()).collect(Collectors.toSet());

        List<Item> filtered = items.all().stream()
                .filter(i -> "published".equalsIgnoreCase(i.status))
                .filter(i -> i.publishedAt != null && i.publishedAt.isAfter(cutoff))
                .filter(i -> topic == null || topic.isBlank() || (i.topics != null && i.topics.contains(topic)))
                .filter(i -> tset.isEmpty() || matches(i, tset))
                .collect(Collectors.toList());

// Simple scoring: text match count + 0.5*recencyBoost (newer is higher)
        Instant now = Instant.now();
        record Scored(Item i, double score) {
        }
        List<Scored> scored = new ArrayList<>();
        for (Item i : filtered) {
            double text = matchCount(i, tset);
            double days = Math.max(1, (double) (now.toEpochMilli() - i.publishedAt.toEpochMilli()) / 86_400_000d);
            double recency = 1.0 / days; // newer -> higher
            scored.add(new Scored(i, text + 0.5 * recency));
        }
        scored.sort(Comparator.comparingDouble((Scored s) -> s.score).reversed());

        List<Item> ordered = scored.stream().map(s -> s.i).toList();
        long total = ordered.size();
        List<Item> slice = com.varta.util.PaginationUtil.page(ordered, page, pageSize);
        List<FeedItem> out = slice.stream().map(this::toFeedItem).toList();
        return new PageResult(out, total);
    }

    private boolean matches(Item i, Set<String> tset) {
        String hay = ((i.title == null ? "" : i.title) + " " + (i.excerpt == null ? "" : i.excerpt)).toLowerCase();
        for (String t : tset) if (hay.contains(t)) return true;
        return false;
    }

    private int matchCount(Item i, Set<String> tset) {
        String hay = ((i.title == null ? "" : i.title) + " " + (i.excerpt == null ? "" : i.excerpt)).toLowerCase();
        int c = 0;
        for (String t : tset) if (hay.contains(t)) c++;
        return c;
    }
}