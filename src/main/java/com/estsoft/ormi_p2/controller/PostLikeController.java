package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;

import com.estsoft.ormi_p2.dto.LikeResponse;
import com.estsoft.ormi_p2.repository.PostRepository;
import com.estsoft.ormi_p2.service.PostLikeService;
import com.estsoft.ormi_p2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final PostRepository postRepository;
    private final UserService userService; // UserService 정의 추가


    // 좋아요 추가 및 취소
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId,
                                        @RequestParam boolean liked,
                                        @AuthenticationPrincipal User user) {
        try {

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

            if (liked) {
                postLikeService.addLike(post, user);
            } else {
                postLikeService.removeLike(post, user);
            }

            long likesCount = postLikeService.getLikeCount(postId);
            boolean isLiked = postLikeService.isLikedByUser(postId, user);

            return ResponseEntity.ok(new LikeResponse(isLiked, likesCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("좋아요 처리에 실패했습니다.");
        }
    }
}
