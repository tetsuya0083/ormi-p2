// src/main/java/com/estsoft/ormi_p2/service/impl/BookmarkServiceImpl.java
package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.repository.BookmarkRepository;
import com.estsoft.ormi_p2.service.BookmarkService;
import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepo;

    @Autowired
    public BookmarkServiceImpl(BookmarkRepository bookmarkRepo) {
        this.bookmarkRepo = bookmarkRepo;
    }

    @Override
    public Page<Post> getBookmarkedPosts(
            Long userId,
            Category category,
            String keyword,
            Pageable pageable
    ) {
        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }
        return bookmarkRepo.findBookmarkedPosts(userId, category, keyword, pageable);
    }
}
