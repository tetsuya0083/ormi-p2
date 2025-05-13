package com.estsoft.ormi_p2.dto;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.PostImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostViewDto {
    private Long id;
    private String title;
    private String thumbnailUrl;
    private String nickname;
    private LocalDateTime createdAt;
    private int likesCount;
    private int viewCount;

    public static PostViewDto fromEntity(Post post) {
        PostViewDto dto = new PostViewDto();
        dto.id = post.getId();
        dto.title = post.getTitle();
        dto.createdAt = post.getCreatedAt();
        dto.likesCount = post.getLikesCount();
        dto.viewCount = post.getViewCount();
        dto.nickname = post.getUser().getNickname();

        dto.thumbnailUrl = post.getImages().stream()
                .map(PostImage::getImageUrl)
                .findFirst()
                .orElse("/img/default-thumbnail.jpg");

        return dto;
    }
}
