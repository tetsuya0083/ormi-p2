package com.estsoft.ormi_p2.dto;

import com.estsoft.ormi_p2.domain.*;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

    private String profileImageUrl;

    private String nickname;

    private String userGrade;

    public PostViewResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.profileImageUrl = post.getUser().getProfileImageUrl();
        this.nickname = post.getUser().getNickname();
        this.userGrade = post.getUser().getUserLevel().getDisplayName();

        // difficulty가 null일 경우 기본값을 할당 (예: "없음")
        this.difficulty = post.getDifficulty() != null ? post.getDifficulty().toString() : "없음";

        // 썸네일 URL 처리
        this.thumbnailUrl = post.getImages().stream()
                // .filter(PostImage::isRepresentImageYn)  // 주석 활성화 시 필요
                .map(PostImage::getImageUrl)
                .findFirst()
                .orElse("/img/default-thumbnail.jpg");

        // tags가 null이거나 비어 있을 경우 빈 리스트로 초기화
        this.tags = post.getTags() != null && !post.getTags().isEmpty()
                ? post.getTags().stream()
                .filter(Objects::nonNull)  // null 태그 객체 제거
                .map(Tag::getName)
                .filter(Objects::nonNull)  // Tag의 name이 null인 경우도 제거
                .collect(Collectors.toList())
                : List.of();  // 태그가 없을 경우 빈 리스트
    }

    @Override
    public String toString() {
        return "PostViewResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                // 필요한 다른 필드들 추가...
                '}';
    }
}