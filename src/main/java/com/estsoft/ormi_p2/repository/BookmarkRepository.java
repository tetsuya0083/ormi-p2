// src/main/java/com/estsoft/ormi_p2/repository/BookmarkRepository.java
package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("""
        select b.post
          from Bookmark b
          left join b.post.tags t
         where b.user.userId    = :userId
           and (:category is null or b.post.category = :category)
           and ( :keyword is null
              or lower(b.post.title) like concat('%', lower(:keyword), '%')
              or lower(t.tag)        like concat('%', lower(:keyword), '%')
             )
         group by b.post
        """)
    Page<Post> findBookmarkedPosts(
            @Param("userId")   Long userId,
            @Param("category") Category category,
            @Param("keyword")  String keyword,
            Pageable pageable
    );
}
