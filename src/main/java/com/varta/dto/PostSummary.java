package com.varta.dto;

import java.time.Instant;

public record PostSummary(String id, String slug, String title, Instant publishedAt) {}