package com.estsoft.ormi_p2.dto;

import com.estsoft.ormi_p2.domain.Comment;
import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddCommentRequest {
    private Long id;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
    private Long userId;
    private Long postId;

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .content(content)
                .user(user)
                .post(post)
                .build();
    }
}
