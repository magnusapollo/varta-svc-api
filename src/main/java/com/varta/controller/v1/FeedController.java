package com.varta.controller.v1;

import com.varta.dto.SearchResult;
import com.varta.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;


    @GetMapping
    @Operation(summary = "Get feed of published items", description = "Sort=top|latest; default latest. Since ISO datetime or PnD period (default P7D). Topic exact match.")
    public ResponseEntity<?> feed(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String since,
            @RequestParam(required = false, defaultValue = "latest") String sort,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int pageSize) {

        int ps = Math.min(Math.max(pageSize, 1), 50);
        var res = feedService.feed(topic, sort, since, Math.max(1, page), ps);
        return ResponseEntity.ok(new SearchResult(res.items(), Math.max(1, page), ps, res.total()));
    }
}