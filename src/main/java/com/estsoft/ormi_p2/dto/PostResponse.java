package com.estsoft.ormi_p2.dto;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.Tag;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private String nickname;

    public PostResponse(Post post) {
        this.id = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

        this.tags = post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        this.nickname = post.getAuthor().getNickname();

    }

}
