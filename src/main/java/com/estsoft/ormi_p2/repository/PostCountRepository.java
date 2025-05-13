package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.dto.CategoryPostCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostCountRepository extends JpaRepository<Post, Long> {
    @Query("SELECT new com.estsoft.ormi_p2.dto.CategoryPostCountDto(p.category, COUNT(p)) " +
            "FROM Post p WHERE p.user.userId = :userId GROUP BY p.category")
    List<CategoryPostCountDto> countPostsByCategory(@Param("userId") Long userId);

}
