package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.PostViewResponse;
import com.estsoft.ormi_p2.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;


@Controller
public class HomeController {
    private final PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
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
        model.addAttribute("newPosts", newPostList);
        model.addAttribute("user", user);

        return "index";
    }
}
