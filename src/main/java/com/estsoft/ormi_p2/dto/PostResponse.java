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

    // 생성자에서 tags 필드도 처리
    public PostResponse(Post post) {
        this.id = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

        // tags 처리
        this.tags = post.getTags().stream()
                .map(Tag::getName)  // Tag 객체에서 이름만 가져와서 리스트에 추가
                .collect(Collectors.toList());

        // 작성자 nickname 처리
        this.nickname = post.getAuthor().getNickname();  // 작성자 nickname

    }

}
