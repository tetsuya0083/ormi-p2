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
    private Difficulty difficulty;
    private LocalDateTime createdAt;
    private String thumbnailUrl;
    private List<String> tags;

    // 작성자 정보
    private String nickname;
    private String userLevel;
    private String profileImageUrl;

    private int likesCount;
    private boolean likesStatus;
    private int viewCount;
    private int commentsCount;

    private String representImageUrl; // 추가: 대표 이미지 URL
    private String userGrade;

    public PostViewResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedAt();
        this.profileImageUrl = post.getUser().getProfileImageUrl();
        this.nickname = post.getUser().getNickname();
        this.userGrade = post.getUser().getUserLevel().getDisplayName();
        this.likesStatus = likesStatus;
        // 난이도
        this.difficulty = post.getDifficulty();
        // 썸네일 이미지
        this.thumbnailUrl = post.getImages().stream()
                .map(PostImage::getImageUrl)
                .findFirst()
                .orElse("/img/default-thumbnail.jpg");

        // 대표 이미지 URL 추가
        this.representImageUrl = post.getImages().stream()
                .filter(Objects::nonNull)
                .map(PostImage::getImageUrl)
                .findFirst()
                .orElse(null);

        // 태그 목록
        this.tags = post.getTags() != null && !post.getTags().isEmpty()
                ? post.getTags().stream()
                .filter(Objects::nonNull)
                .map(Tag::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                : List.of();

        // 작성자 정보
        User user = post.getUser();
        this.nickname = user.getNickname();
        this.userLevel = user.getUserLevel() != null ? user.getUserLevel().getDisplayName() : "텅빈 냉장고 요정";
        this.profileImageUrl = user.getProfileImageUrl() != null ? user.getProfileImageUrl() : "/img/default-profile.png";

        this.likesCount = post.getLikesCount();
        this.viewCount = post.getViewCount();
        this.commentsCount = post.getCommentsCount();
    }

    @Override
    public String toString() {
        return "PostViewResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", nickname='" + nickname + '\'' +
                ", likesCount=" + likesCount +
                ", likesStatus=" + likesStatus +
                ", commentsCount=" + commentsCount +
                ", representImageUrl='" + representImageUrl + '\'' +
                '}';
    }
}
