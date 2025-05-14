package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.PostViewResponse;
import com.estsoft.ormi_p2.service.PostService;
import com.estsoft.ormi_p2.service.BookmarkService;   // 추가
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {
    private final PostService postService;
    private final BookmarkService bookmarkService;  // 추가

    // ← 기존 public HomeController(PostService postService) { … } 코드는 모두 지우고
    // 아래 단일 생성자만 남겨주세요.
    public HomeController(PostService postService,
                          BookmarkService bookmarkService) {
        this.postService = postService;
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal User user) {
        List<PostViewResponse> newPostList = postService.getAllPosts()
                .stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed()) // 최신순 정렬
                .limit(10)
                .map(PostViewResponse::new)
                .toList();

        List<PostViewResponse> likedPostList = postService.getAllPosts()
                .stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed()) // 최신순 정렬
                .limit(6)
                .map(PostViewResponse::new)
                .toList();

        model.addAttribute("likedPosts", likedPostList);
        model.addAttribute("newPosts",   newPostList);
        model.addAttribute("user",       user);

        // 로그인한 사용자의 북마크된 포스트 ID 목록
        if (user != null) {
            List<Long> bookmarkedPostIds = bookmarkService.getBookmarkedPostIds(user.getUserId());
            model.addAttribute("bookmarkedPostIds", bookmarkedPostIds);
        } else {
            model.addAttribute("bookmarkedPostIds", List.<Long>of());
        }

        return "index";
    }
}