package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.Comment;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.AddCommentRequest;
import com.estsoft.ormi_p2.dto.CommentResponse;
import com.estsoft.ormi_p2.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {

    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> save(
            @PathVariable Long postId,
            @Valid @RequestBody AddCommentRequest request,
            @AuthenticationPrincipal User user // 인증된 User 객체를 자동으로 받아옴
    ) {
        Comment savedComment = commentService.save(postId, request, user.getLoginId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedComment);
    }

    //댓글 읽어오기
    @GetMapping("/posts/{postId}/comments")
    public List<Comment> read(@PathVariable long postId) {
        return commentService.findAll(postId);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody AddCommentRequest request,
            @AuthenticationPrincipal User user // @AuthenticationPrincipal을 사용하여 User 객체를 받아옴
    ) {
        Comment updated = commentService.update(postId, commentId, request, user.getLoginId()); // User 객체 전달
        return ResponseEntity.ok(updated);
    }

    //댓글 삭제
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Long> delete(@PathVariable long postId, @PathVariable Long commentId, @AuthenticationPrincipal User user) {
        commentService.delete(postId, commentId, user); // User 객체 전달
        return ResponseEntity.ok(commentId);
    }

}
