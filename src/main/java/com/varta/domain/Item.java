package com.varta.domain;

import java.time.Instant;
import java.util.List;

public class Item {
    public String id;
    public String slug;
    public String title;
    public String author;
    public String url;
    public String canonicalUrl;
    public SourceRef source;
    public Instant publishedAt;
    public String excerpt;
    public String status; // published | draft
    public List<String> topics;
}