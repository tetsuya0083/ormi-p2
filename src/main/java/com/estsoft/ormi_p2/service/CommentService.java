package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.domain.Comment;
import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.AddCommentRequest;
import com.estsoft.ormi_p2.repository.CommentRepository;
import com.estsoft.ormi_p2.repository.PostRepository;
import com.estsoft.ormi_p2.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 댓글 추가 메서드
    @Transactional
    public Comment save(Long postId, AddCommentRequest request, String loginId) {
        // 사용자가 존재하는지 확인
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다: " + loginId));

        // 게시글이 존재하는지 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + postId));

        // 댓글 내용 검증
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 필수입니다.");
        }

        // 댓글 엔티티 생성 및 저장
        Comment comment = request.toEntity(user, post);
        return commentRepository.save(comment);
    }

    // 댓글을 불러온다
    @Transactional
    public List<Comment> findAll(Long postId) {

        // 게시글이 존재하는지 확인
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: " + postId));

        // 게시글에 달린 댓글 목록 반환
        return commentRepository.findByPost_PostId(postId);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long postId, Long commentId, User user) {
        // 해당 댓글이 존재하는지 확인
        Comment comment = commentRepository.findByPost_IdAndCommentId(postId, commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + commentId));

        // 댓글 삭제
        commentRepository.delete(comment);
    }

    // 게시글 작성자인지 확인
    private static void authorizedArticleAuthor(Post post) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!post.getUser().getLoginId().equals(userName)) {
            throw new IllegalStateException("이 게시글의 작성자가 아닙니다.");
        }
    }

    // 댓글 수정
    @Transactional
    public Comment update(Long postId, Long commentId, AddCommentRequest request, String loginId) {
        // 해당 게시글과 댓글이 존재하는지 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + postId));
        Comment comment = commentRepository.findByPost_IdAndCommentId(postId, commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + commentId));

        // 댓글을 작성한 사용자인지 확인
        if (!comment.getUser().getLoginId().equals(loginId)) {
            throw new IllegalStateException("이 댓글의 작성자가 아닙니다.");
        }

        // 댓글 내용 검증
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 필수입니다.");
        }

        // 댓글 수정
        comment.setContent(request.getContent());  // 댓글 내용 수정
        comment.setUpdatedAt(LocalDateTime.now());  // 수정 시간 갱신

        // 수정된 댓글 저장
        return commentRepository.save(comment);
    }
}
