package com.estsoft.ormi_p2.dto;

import com.estsoft.ormi_p2.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String nickname;
    private Long userId;
    private Long postId;
    private String userLevel;

    // Entity -> DTO
    public CommentResponse(Comment comment) {
        this.id = comment.getCommentId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.postId = comment.getPost().getId();
        this.nickname = comment.getUser().getNickname();
        this.userId = comment.getUser().getUserId();
        this.userLevel = comment.getUser().getUserLevel().name();
    }
}
