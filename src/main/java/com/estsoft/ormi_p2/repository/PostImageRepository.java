package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPostId(Long postId);

    void deleteByPostId(Long postId);

    // 해당 게시글의 이미지 개수
    int countByPost(Post post);
}
