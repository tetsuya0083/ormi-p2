package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> findByPost_IdAndCommentId(Long postId, Long commentId);

    List<Comment> findByPost_PostId(Long postId);
}