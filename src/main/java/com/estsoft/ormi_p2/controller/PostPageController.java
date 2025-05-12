package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.PostViewResponse;
import com.estsoft.ormi_p2.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class PostPageController {

    private final PostService postService;

    public PostPageController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 작성 페이지 (GET)
    @GetMapping("/new-post")
    public String showBlogEditPage(@RequestParam(required = false) Long id, Model model,
                                   @AuthenticationPrincipal User user) {
        model.addAttribute("categories", Category.values());
        model.addAttribute("user", user);


        if (id == null) {
            model.addAttribute("post", new PostViewResponse());
        } else {
            Post post = postService.findPost(id);

            if (post == null) {
                post = new Post();
            }

            model.addAttribute("post", new PostViewResponse(post));
        }

        return "newPost";
    }

    // 게시글 저장 (POST)
//    @PostMapping("/new-post")
//    public String savePost(@RequestParam String title,
//                           @RequestParam String content,
//                           @RequestParam String category,
//                           @RequestParam String difficulty,
//                           @RequestParam(required = false) String tagString,
//                           @RequestParam(required = false) MultipartFile image,
//                           Model model) {
//        try {
//            Post savedPost = postService.savePost(title, content, category, difficulty, tagString, image);
//            return "redirect:/posts"; // 게시글 목록 페이지로 이동
//        } catch (Exception e) {
//            model.addAttribute("error", "게시글 저장에 실패했습니다.");
//            return "newPost"; // 다시 작성 페이지로
//        }
//    }

    // 게시글 목록 페이지 (GET)
    @GetMapping("/posts/list")
    public String showPostList(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "postList";  // 게시글 목록 페이지로 이동
    }

    // 게시글 목록 페이지 (GET) - DTO로 변환된 게시글 목록
    @GetMapping("/posts")
    public String getPosts(Model model, @AuthenticationPrincipal User user) {
        List<PostViewResponse> postList = postService.getAllPosts()
                .stream().map(PostViewResponse::new)
                .toList();

        model.addAttribute("posts", postList);
        model.addAttribute("user", user);

        return "postList";  // html 페이지
    }

}
