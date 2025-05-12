package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.PostViewResponse;
import com.estsoft.ormi_p2.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
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

    @GetMapping("/myPosts")
    public String showMyPosts(Model model, @AuthenticationPrincipal User user,
                              @RequestParam(value="page", defaultValue="0") int page) {
        List<PostViewResponse> myPostList = postService.getPostsByUser(user)
                .stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed()) // 최신순 정렬
                .limit(3)
                .map(PostViewResponse::new)
                .toList();

        model.addAttribute("myPosts", myPostList);
        model.addAttribute("user", user);

        Page<PostViewResponse> paging = postService.getPostsByUserId(user.getUserId(), page);
        model.addAttribute("paging", paging);;

        return "myPosts";
    }
}
