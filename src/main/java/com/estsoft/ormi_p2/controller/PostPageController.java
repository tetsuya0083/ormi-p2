package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.domain.Comment;
import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.PostViewResponse;
import com.estsoft.ormi_p2.dto.UpdatePostRequest;
import com.estsoft.ormi_p2.service.CommentService;
import com.estsoft.ormi_p2.service.PostLikeService;
import com.estsoft.ormi_p2.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
public class PostPageController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;


    public PostPageController(PostService postService, PostLikeService postLikeService, CommentService commentService) {
        this.postService = postService;
        this.postLikeService = postLikeService;
        this.commentService = commentService;
    }

    // 게시글 작성 페이지 (GET)
    @GetMapping("/new-post")
    public String showBlogEditPage(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("categories", Category.values());

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
        return "postList";
    }

    // 게시글 저장 (POST) - 로그인 사용자 연동
    @PostMapping("/new-post")
    public String savePost(@RequestParam String title,
                           @RequestParam String content,
                           @RequestParam String category,
                           @RequestParam String difficulty,
                           @RequestParam(required = false) String tagString,
                           @RequestParam(required = false) MultipartFile image,
                           @AuthenticationPrincipal User user,  // 로그인 사용자 주입
                           Model model) {
        try {
            Post savedPost = postService.savePost(title, content, category, difficulty, tagString, image, user);
            return "redirect:/posts"; // 게시글 목록 페이지로 이동
        } catch (Exception e) {
            model.addAttribute("error", "게시글 저장에 실패했습니다.");
            return "newPost";
        }
    }

    // 게시글 목록 페이지 (GET)
    @GetMapping("/posts")
    public String getPosts(Model model, @AuthenticationPrincipal User user) {
        List<PostViewResponse> postList = postService.getAllPosts()
                .stream().map(PostViewResponse::new)
                .toList();

        model.addAttribute("posts", postList);

        if (user != null) {
            model.addAttribute("user", user);
        }

        return "postList";
    }

    // 단건 조회 GET /posts/{id}
    @GetMapping("/posts/{postId}")
    public String showPostDetail(@PathVariable Long postId, @AuthenticationPrincipal User user, Model model) {
        Post post = postService.getPost(postId);
        List<Comment> comments = commentService.findAll(postId);
        long likesCount = postLikeService.getLikeCount(postId);
        boolean isLiked = user != null && postLikeService.isLikedByUser(postId, user);

        postService.incrementViewCount(postId);

        // 작성자 확인 (본인만 수정/삭제 가능)
        boolean isAuthor = post.getAuthor().getUsername().equals(user != null ? user.getUsername() : "");

        model.addAttribute("post", new PostViewResponse(post));
        model.addAttribute("comments", comments);
        model.addAttribute("likesCount", likesCount);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("isAuthor", isAuthor); // 본인인지 여부 추가

        return "postDetail";
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/posts";  // 게시글 목록 페이지로 리다이렉트
    }

    // 게시글 수정 페이지 이동
    @GetMapping("/posts/edit/{postId}")
    public String editPost(@PathVariable Long postId,
                           Model model,
                           @AuthenticationPrincipal User user) throws AccessDeniedException {
        Post post = postService.getPost(postId);

        // 작성자 확인 (userId 비교)
        if (!post.getAuthor().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("이 글을 수정할 권한이 없습니다.");
        }

        UpdatePostRequest updatePostRequest = new UpdatePostRequest(post);
        model.addAttribute("post", updatePostRequest);

        return "newPost";
    }

    @PutMapping("/posts/{postId}")
    public String updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest updatePostRequest) {
        postService.updatePost(postId, updatePostRequest);
        return "redirect:/posts/" + postId;
    }

    // 게시글 삭제 (특정 권한 체크)
    @GetMapping("/posts/delete/{postId}")
    public String deletePost(@PathVariable Long postId,
                             @AuthenticationPrincipal User user) throws AccessDeniedException {
        Post post = postService.getPost(postId);

        // 작성자 확인
        if (!post.getAuthor().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("이 글을 삭제할 권한이 없습니다.");
        }

        postService.deletePost(postId);
        return "redirect:/posts";
    }
}
