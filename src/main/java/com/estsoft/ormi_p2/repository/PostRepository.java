package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.PostViewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUser(User user);

    @Query("SELECT new com.estsoft.ormi_p2.dto.PostViewResponse(p) FROM Post p WHERE p.user.userId = :userId")
    Page<PostViewResponse> findByUserId(@Param("userId") Long userId, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findByCategory(String category, Pageable pageable);

}
