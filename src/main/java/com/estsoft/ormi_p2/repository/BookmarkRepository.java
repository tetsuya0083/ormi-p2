package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.Bookmark;
import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    // ------------------- 여기에 추가 -------------------

    /** 사용자가 이 포스트를 북마크했는지 체크 */
    boolean existsByUser_UserIdAndPost_PostId(Long userId, Long postId);

    /** 사용자별 북마크 전체 조회 */
    List<Bookmark> findByUser_UserId(Long userId);

    /** 사용자-포스트 조합으로 북마크 삭제 */
    void deleteByUser_UserIdAndPost_PostId(Long userId, Long postId);
}
