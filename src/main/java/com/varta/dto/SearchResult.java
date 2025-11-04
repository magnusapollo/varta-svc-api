package com.varta.dto;

import java.util.List;

public record SearchResult(List<FeedItem> results, int page, int pageSize, long total) {
}