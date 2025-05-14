package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.domain.*;
import com.estsoft.ormi_p2.dto.AddPostRequest;
import com.estsoft.ormi_p2.dto.PostViewResponse;
import com.estsoft.ormi_p2.dto.PostResponse;
import com.estsoft.ormi_p2.dto.PostViewDto;
import com.estsoft.ormi_p2.dto.UpdatePostRequest;
import com.estsoft.ormi_p2.exception.NotExistsIdException;
import com.estsoft.ormi_p2.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final TagRepository tagRepository;
    private final UserService userService;

    @Autowired
    public PostService(UserRepository userRepository,
                       PostRepository postRepository,
                       PostImageRepository postImageRepository,
                       PostKeywordRepository postKeywordRepository,
                       TagService tagService, UserService userService,
                       TagRepository tagRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
        this.postKeywordRepository = postKeywordRepository;
        this.tagService = tagService;
        this.userService = userService;
        this.tagRepository = tagRepository;

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

        User user = userRepository.findByLoginId("").orElse(new User());
        post.setUser(user);

        Post savedPost = postRepository.save(post);

        // 대표 이미지 저장
        //savePostImage(savedPost, request.getRepresentImageUrl(), true);

        // 일반 이미지들 저장
//        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
//            for (String imageUrl : request.getImageUrl()) {
//                savePostImage(savedPost, imageUrl, false);
//            }
//        }

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
                         String difficulty, String tagString, MultipartFile image,
                         User user) {
        Category categoryEnum = Category.valueOf(category.toUpperCase());
        Difficulty difficultyEnum = processDifficulty(categoryEnum, difficulty);

        List<Tag> tagList = processTags(Arrays.asList(tagString.split(",")));
        Post post = new Post(title, content, categoryEnum, difficultyEnum, tagList, user);
        Post savedPost = postRepository.save(post);

        Long keywordId = 1L;

        for (Tag tag : tagList) {
            PostKeyword postKeyword = new PostKeyword(savedPost, tag, keywordId);
            postKeywordRepository.save(postKeyword);  // PostKeyword 저장
        }

        // 이미지 저장
        if (image != null) {
            savePostImage(savedPost, image, false);  // 이미지가 있을 때만 저장
        }

        post.setDifficultyBasedOnCategory(post.getDifficulty());

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

    // MultipartFile 이미지 저장
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

    public List<Post> getPostsByUser(User user) {
        return postRepository.findAllByUser(user);
    }

    public Page<PostViewResponse> getPostsByUserId(Long userId, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));

        Pageable pageable = PageRequest.of(page, 3, Sort.by(sorts));
        return postRepository.findByUserId(userId, pageable);
    }

    public List<Tag> processTags(List<String> tagStrings) {
        List<Tag> tagList = new ArrayList<>();

        for (String tagName : tagStrings) {
            Tag tag = tagService.getOrCreateTag(tagName);  // 태그를 가져오거나 새로 생성
            tagList.add(tag);
        }

        return tagList;
    }

    // post 단건 조회 id 기준으로 조회
    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no exists id: " + id));
    }


    public void incrementViewCount(Long postId) {
        Post post = getPost(postId);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }

    // 게시글 수정 메소드 PUT /api/posts/{postId} 에서 넘어옴
    @Transactional
    public Post updatePost(Long id, UpdatePostRequest request, MultipartFile image)
    throws IOException {
        Post post = postRepository.findById(id)     //  이 시점에 post는 영속 상태
                .orElseThrow(() -> new NotExistsIdException(id)); // 500 Error

        // 새로운 title, content로 교체
        post.update(request.getTitle(), request.getContent());

        post.setCategory(Category.valueOf(request.getCategory())); // 카테고리 업데이트
        post.setDifficulty(
                request.getDifficulty() != null ? Difficulty.valueOf(request.getDifficulty()) : null
        );

        /*
        List<PostKeyword> newPostKeywords = request.getTags().stream()
                .map(tagName -> {
                    Tag tag = tagRepository.findByTag(tagName)
                            .orElseGet(() -> tagRepository.save(new Tag(tagName))); // 없으면 새로 생성
                    return new PostKeyword(post, tag, 1L); // 1L은 기본 weight 예시
                })
                .collect(Collectors.toList());

        post.getPostKeywords().clear();
        post.getPostKeywords().addAll(newPostKeywords);

        List<PostImage> newImages = request.getImageUrl().stream()
                .map(url -> new PostImage(url, post)) // 새 이미지를 생성
                .collect(Collectors.toList());
        */

        post.getImages().clear();

        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads"); // 현재 프로젝트 기준 절대 경로
        Files.createDirectories(uploadDir);

        // 클라이언트가 접근할 수 있는 URL 생성
        Path fullPath = uploadDir.resolve(filename);  // uploads/파일명 전체 경로
        image.transferTo(fullPath.toFile());   // 절대 경로 넘김!

        String imageUrl = "/images/" + filename;
        PostImage postImage = new PostImage(imageUrl, post);
        postImage.setPost(post);
        post.getImages().add(postImage);

//        post.getImages().addAll(imageUrl);

//        if (!newImages.isEmpty()) {
//            post.setImages(newImages.get(0).getUrl()); // 첫 번째 이미지를 대표 이미지로 설정
//        }

        // 변경된 게시글 저장
        //return postRepository.save(post);
        return post;  // 이 return은 영속 상태 객체
    }

    // 게시글 삭제
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public Page<Post> getPostsByCategory(String category, PageRequest pageRequest) {
        return postRepository.findByCategory(category, pageRequest);  // 카테고리 필터링
    }


}
