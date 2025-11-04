package com.varta.util;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;

public class TimeUtil {

    /**
     * Parses the "since" query parameter.
     * Supports either an ISO-8601 datetime (e.g. 2025-10-28T14:32:00Z)
     * or a duration period like "P7D" (last 7 days).
     * Defaults to now minus 7 days if invalid or missing.
     */
    public static Instant parseSince(String sinceParam) {
        if (sinceParam == null || sinceParam.isBlank()) {
            return Instant.now().minus(Duration.ofDays(7));
        }
        try {
            // Period form like P7D, P1M, etc.
            if (sinceParam.startsWith("P")) {
                Period period = Period.parse(sinceParam);
                long days = period.getDays() + (period.getMonths() * 30L) + (period.getYears() * 365L);
                return Instant.now().minus(Duration.ofDays(days));
            }
            // ISO datetime form
            return Instant.parse(sinceParam);
        } catch (Exception e) {
            return Instant.now().minus(Duration.ofDays(7));
        }
    }
}
