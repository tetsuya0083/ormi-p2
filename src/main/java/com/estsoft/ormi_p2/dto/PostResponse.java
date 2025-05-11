package com.estsoft.ormi_p2.dto;

import com.estsoft.ormi_p2.domain.Post;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostResponse {
    private Long id;
    private String title;
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<String> tags = new ArrayList<>();

    public PostResponse(Post post) {
        this.id = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}