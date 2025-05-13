package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.PostLikes;
import com.estsoft.ormi_p2.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
    Optional<PostLikes> findByPostIdAndUserUserId(Long postId, Long userId);  // 수정된 부분
    long countByPostId(Long postId);
    void deleteByPostIdAndUserUserId(Long postId, Long userId);
    boolean existsByPostIdAndUserUserId(Long postId, Long userId); // 수정된 부분
    Optional<PostLikes> findByPostAndUser(Post post, User user);
}