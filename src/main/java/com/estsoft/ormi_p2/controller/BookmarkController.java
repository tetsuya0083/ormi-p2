// src/main/java/com/estsoft/ormi_p2/controller/BookmarkController.java
package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/bookmarks")
    public String listBookmarks(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        // ① userId 추출
        Long userId = user.getUserId();

        // ② 북마크 조회
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = bookmarkService.getBookmarkedPosts(userId, category, keyword, pageable);

        // ③ 모델에 필요한 속성들
        model.addAttribute("user",      user);
        model.addAttribute("posts",     posts);
        model.addAttribute("category",  category);
        model.addAttribute("keyword",   keyword);
        model.addAttribute("categories",Category.values());
        model.addAttribute("sizes",     new int[]{10,20,50,100});

        return "bookmark";
    }
}
