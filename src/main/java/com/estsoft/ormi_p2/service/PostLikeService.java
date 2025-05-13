package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.PostLikes;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.repository.PostLikesRepository;
import com.estsoft.ormi_p2.repository.PostRepository;
import com.estsoft.ormi_p2.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikesRepository postLikesRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleLike(Long postId, User user) {
        validateUser(user);
        Post post = getPost(postId);

        return postLikesRepository.findByPostIdAndUserUserId(postId, user.getUserId())
                .map(existingLike -> {
                    postLikesRepository.delete(existingLike);
                    return false; // 좋아요 삭제
                })
                .orElseGet(() -> {
                    postLikesRepository.save(new PostLikes(post, user));
                    return true; // 좋아요 추가
                });
    }

    public long getLikeCount(Long postId) {
        return postLikesRepository.countByPostId(postId);
    }

    public boolean isLikedByUser(Long postId, User user) {
        validateUser(user);
        return postLikesRepository.existsByPostIdAndUserUserId(postId, user.getUserId());
    }

    @Transactional
    public void addLike(Post post, User user) {
        validateUser(user);
        boolean alreadyLiked = postLikesRepository.existsByPostIdAndUserUserId(post.getId(), user.getUserId());
        if (alreadyLiked) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
        }
        postLikesRepository.save(new PostLikes(post, user));
    }

    @Transactional
    public void removeLike(Post post, User user) {
        validateUser(user);

        // 좋아요를 누른 상태에서 취소하는 로직
        PostLikes postLikes = postLikesRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new PostLikeNotFoundException("좋아요를 누른 게시글이 아닙니다."));

        postLikesRepository.delete(postLikes);  // 좋아요 취소 처리
    }

    private void validateUser(User user) {
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

    public static class PostLikeNotFoundException extends RuntimeException {
        public PostLikeNotFoundException(String message) {
            super(message);
        }
    }
}
