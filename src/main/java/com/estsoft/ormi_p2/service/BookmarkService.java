// src/main/java/com/estsoft/ormi_p2/service/BookmarkService.java
package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkService {
    Page<Post> getBookmarkedPosts(
            Long userId,
            Category category,
            String keyword,
            Pageable pageable
    );
}
