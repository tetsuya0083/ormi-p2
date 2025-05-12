package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.dto.AddPostRequest;
import com.estsoft.ormi_p2.dto.PostResponse;
import com.estsoft.ormi_p2.dto.PostViewResponse;
import com.estsoft.ormi_p2.repository.PostRepository;
import com.estsoft.ormi_p2.service.PostService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public PostController(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    // 게시글 작성 (form 요청용)
    @PostMapping("/new-post")
    public String createPost(@ModelAttribute AddPostRequest request) {
        postService.createPost(request);
        return "redirect:/posts";
    }

    // 게시글 저장 (JSON 요청 + Multipart 이미지 업로드)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> savePost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") String category,
            @RequestParam(value = "difficulty", required = false) String difficulty,
            @RequestParam("tags") String tags,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {
        //
        System.out.println("=== 게시글 저장 시도 ===");
        System.out.println("제목: " + title);
        System.out.println("카테고리: " + category);
        System.out.println("내용: " + content);
        System.out.println("난이도: " + difficulty);
        System.out.println("태그: " + tags);
        System.out.println("대표 이미지: " + (image != null ? image.getOriginalFilename() : "null"));

        Post savedPost = postService.savePost(title, content, category, difficulty, tags, image);
        return ResponseEntity.ok(new PostResponse(savedPost));
    }


    // 게시글 목록 조회
    @GetMapping
    public List<PostViewResponse> getPostList() {
        return postService.getAllPosts().stream()
                .map(PostViewResponse::new)
                .collect(Collectors.toList());
    }
}
