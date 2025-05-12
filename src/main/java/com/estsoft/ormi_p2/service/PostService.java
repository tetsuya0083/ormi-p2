package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.domain.*;
import com.estsoft.ormi_p2.dto.AddPostRequest;
import com.estsoft.ormi_p2.exception.NotExistsIdException;
import com.estsoft.ormi_p2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final TagService tagService;
    private final PostKeywordRepository postKeywordRepository;

    @Autowired
    public PostService(UserRepository userRepository,
                       PostRepository postRepository,
                       PostImageRepository postImageRepository,
                       PostKeywordRepository postKeywordRepository,
                       TagService tagService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
        this.postKeywordRepository = postKeywordRepository;
        this.tagService = tagService;
    }

    @Transactional
    public void createPost(AddPostRequest request) {
        Category category = Category.valueOf(request.getCategory());
        Difficulty difficulty = processDifficulty(category, request.getDifficulty());

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setCategory(category);
        post.setContent(request.getContent());
        post.setDifficulty(difficulty);

        User user = userRepository.findByUserId(1L).orElse(new User());
        post.setUser(user);

        Post savedPost = postRepository.save(post);

        // 대표 이미지 저장
        savePostImage(savedPost, request.getRepresentImageUrl(), true);

        // 일반 이미지들 저장
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            for (String imageUrl : request.getImageUrl()) {
                savePostImage(savedPost, imageUrl, false);
            }
        }

        // 태그 저장 (기존 태그 재사용 X)
        Long keywordId = 1L;
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            for (String tagName : request.getTags()) {
                Tag tag = tagService.getOrCreateTag(tagName);

                PostKeyword postKeyword = new PostKeyword(savedPost, tag, keywordId);

                postKeywordRepository.save(postKeyword);
            }
        }
    }

    @Transactional
    public Post savePost(String title, String content, String category,
                         String difficulty, String tagString, MultipartFile image) {
        Category categoryEnum = Category.valueOf(category.toUpperCase());
        Difficulty difficultyEnum = processDifficulty(categoryEnum, difficulty);

        List<Tag> tagList = processTags(Collections.singletonList(tagString));

        User user = getUserInfo();

        Post post = new Post(title, content, categoryEnum, difficultyEnum, tagList, user);
        Post savedPost = postRepository.save(post);

        Long keywordId = 1L;
        // 태그와 게시글의 관계 처리 (PostKeyword 생성)
        for (Tag tag : tagList) {
            PostKeyword postKeyword = new PostKeyword(savedPost, tag, keywordId);
            postKeywordRepository.save(postKeyword);  // PostKeyword 저장
        }

        // 이미지 저장
        savePostImage(savedPost, image, false);  // 이미지가 있을 때만 저장

        return savedPost;
    }

    // 난이도 처리 (SHARE 카테고리일 때만 난이도 필수)
    private Difficulty processDifficulty(Category categoryEnum, String difficulty) {
        if (categoryEnum == Category.SHARE) {
            if (difficulty == null || difficulty.isEmpty()) {
                throw new IllegalArgumentException("SHARE 카테고리에는 난이도가 필수입니다.");
            }
            return Difficulty.valueOf(difficulty.toUpperCase());
        } else {
            return Difficulty.EASY; // 기본값은 EASY
        }
    }

    private void savePostImage(Post savedPost, String imageUrl, boolean isRepresentative) {
        // 이미지가 없을 경우 기본 이미지 사용
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = "/img/default-thumbnail.jpg"; // 기본 이미지 경로
        }

        // 저장할 이미지 정보 설정
        PostImage postImage = PostImage.builder()
                .post(savedPost)
                .imageUrl(imageUrl)
                .representImageYn(isRepresentative)
                .build();

        postImageRepository.save(postImage);
    }

    private void savePostImage(Post savedPost, MultipartFile image, boolean isRepresentative) {
        // 이미지가 없을 경우 기본 이미지 사용
        String imageUrl = (image == null || image.isEmpty()) ? "/img/default-thumbnail.jpg" : saveImageToFile(image);

        // 저장할 이미지 정보 설정
        PostImage postImage = PostImage.builder()
                .post(savedPost)
                .imageUrl(imageUrl)
                .representImageYn(isRepresentative)
                .build();

        postImageRepository.save(postImage);
    }

    // 이미지 파일을 서버에 저장하고 URL 반환
    private String saveImageToFile(MultipartFile image) {
        try {
            String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            String uploadDir = System.getProperty("user.dir") + "/uploads/posts/";
            Path imagePath = Paths.get(uploadDir);

            Files.createDirectories(imagePath);
            Path fullPath = imagePath.resolve(imageName);

            image.transferTo(fullPath.toFile());

            return "/images/posts/" + imageName;  // 이미지가 있는 경우 URL 설정
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장에 실패했습니다.", e);
        }
    }

    public Post findPost(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Tag> processTags(List<String> tagStrings) {
        List<Tag> tagList = new ArrayList<>();

        for (String tagName : tagStrings) {
            Tag tag = tagService.getOrCreateTag(tagName);  // 태그를 가져오거나 새로 생성
            tagList.add(tag);
        }

        return tagList;
    }

    private User getUserInfo() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new NotExistsIdException(1L));
    }
}
