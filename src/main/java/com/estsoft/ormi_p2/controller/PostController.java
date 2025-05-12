package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.AddPostRequest;
import com.estsoft.ormi_p2.dto.PostResponse;
import com.estsoft.ormi_p2.dto.PostViewResponse;
import com.estsoft.ormi_p2.repository.PostRepository;
import com.estsoft.ormi_p2.service.PostService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    // 게시글 목록 조회
    @GetMapping
    public List<PostViewResponse> getPostList() {
        return postService.getAllPosts().stream()
                .map(PostViewResponse::new)
                .collect(Collectors.toList());
    }
}
