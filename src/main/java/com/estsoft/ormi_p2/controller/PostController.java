package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.Category;
import com.estsoft.ormi_p2.domain.Comment;
import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.AddPostRequest;
import com.estsoft.ormi_p2.dto.PostResponse;
import com.estsoft.ormi_p2.dto.PostViewResponse;
import com.estsoft.ormi_p2.dto.*;
import com.estsoft.ormi_p2.repository.PostRepository;
import com.estsoft.ormi_p2.repository.UserRepository;
import com.estsoft.ormi_p2.service.PostService;
import com.estsoft.ormi_p2.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public PostController(PostService postService, PostRepository postRepository, UserService userService, UserRepository userRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // 게시글 저장 (JSON 요청 + Multipart 이미지 업로드)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> savePost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") String category,
            @RequestParam(value = "difficulty", required = false) String difficulty,
            @RequestParam("tags") String tags,
            @RequestParam(value = "imageInput", required = false) MultipartFile image,
            @AuthenticationPrincipal User user
    ) throws IOException {
        Post savedPost = postService.savePost(title, content, category, difficulty, tags, image, user);
        return ResponseEntity.ok(new PostResponse(savedPost));
    }

    @GetMapping
    public List<PostViewResponse> getPostList() {
        return postService.getAllPosts().stream()
                .map(PostViewResponse::new)
                .collect(Collectors.toList());
    }

    // 게시글 수정 페이지로 이동
    @GetMapping("/new-post")
    public String editPost(@RequestParam Long postId, Model model) {
        Post post = postService.getPost(postId);
        model.addAttribute("post", post);
        return "newPost";
    }

    @PostMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable("postId") Long postId,
                                           @ModelAttribute UpdatePostRequest request,
                                           @RequestParam("image") MultipartFile image) {
        try {
            postService.updatePost(postId, request, image);

        } catch (IOException e) {
            throw new RuntimeException("게시글 수정 실패", e);
        }

//        PostResponse response = post.toDto();
//        return ResponseEntity.ok(response);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        Post post = postService.getPost(postId);
        if (!post.getAuthor().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(403).build();
        }
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/post/list/{category}")
    public String getPostListByCategory(
            @PathVariable Category category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "latest") String sort,
            Model model) {

        String categoryName = category.name();
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Order.asc(sort)));
        Page<Post> posts = postService.getPostsByCategory(categoryName, pageRequest);

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("category", categoryName);

        return "postList";
    }


}