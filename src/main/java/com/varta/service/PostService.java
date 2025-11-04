package com.varta.service;

import com.varta.domain.Post;
import com.varta.repo.PostRepo;
import java.util.List;
import java.util.Optional;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepo posts;
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    public PostService(PostRepo posts) { this.posts = posts; }

    public List<Post> all() { return posts.all(); }
    public Optional<Post> bySlug(String slug) { return posts.bySlug(slug); }

    public String renderMd(String markdown) {
        Node node = parser.parse(markdown == null ? "" : markdown);
        return renderer.render(node);
    }
}