package com.estsoft.ormi_p2.dto;

import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.PostImage;
import com.estsoft.ormi_p2.domain.Tag;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostViewResponse {
    private Long id;
    private String title;
    private String content;
    private Category category;
    private String difficulty;

    private List<String> tags;

    private LocalDateTime createdAt;

    private String thumbnailUrl;

    public PostViewResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.difficulty = post.getDifficulty() != null ? post.getDifficulty().toString() : null;

        this.thumbnailUrl = post.getImages().stream()
                .filter(PostImage::isRepresentImageYn)
                .map(PostImage::getImageUrl)
                .findFirst()
                .orElse("/img/default-thumbnail.jpg");

        this.tags = post.getTags() != null && !post.getTags().isEmpty()
                ? post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList())
                : List.of();

    }
}