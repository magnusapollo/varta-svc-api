package com.varta.domain;


import java.time.Instant;

public class Post {
    public String id;
    public String slug;
    public String title;
    public String markdown; // stored md
    public String html; // pre-rendered (mock)
    public Instant publishedAt;
}