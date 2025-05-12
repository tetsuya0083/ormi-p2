package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostKeywordRepository  extends JpaRepository<PostKeyword, Long> {
    List<PostKeyword> findByPostId(Long postId);
    void deleteByPostId(Long postId);
}
