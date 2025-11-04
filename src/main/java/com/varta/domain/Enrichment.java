package com.varta.domain;

import java.util.List;
import java.util.Map;

public class Enrichment {
    public String itemId; // FK
    public String summaryBullets; // markdown list string
    public String whyItMatters;
    public Map<String, List<String>> entities; // people/orgs/libs
    public List<String> topics;
}