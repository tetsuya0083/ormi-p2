package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookmarkService {
    Page<Post> getBookmarkedPosts(Long userId, Category category, String keyword, Pageable pageable);

    // ------------------- 여기에 추가 -------------------
    /** 북마크 추가 */
    void addBookmark(Long userId, Long postId);

    /** 북마크 해제 */
    void removeBookmark(Long userId, Long postId);

    /** 사용자가 북마크한 포스트 ID 목록 */
    List<Long> getBookmarkedPostIds(Long userId);
}
