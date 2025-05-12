package com.estsoft.ormi_p2.domain;

import com.estsoft.ormi_p2.dto.PostResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @Column(name = "likes_count", nullable = false, columnDefinition = "int default 0")
    private int likesCount = 0;

    @Column(name = "comments_count", nullable = false, columnDefinition = "int default 0")
    private int commentsCount = 0;

    @Column(name = "is_public", nullable = false, columnDefinition = "tinyint default 1")
    private boolean isPublic = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostKeyword> postKeywords = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> images;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 기존 생성자
    public Post(String title, String content, Category category, Difficulty difficulty, List<Tag> tags, User user) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.difficulty = difficulty;
        this.postKeywords = tags != null
                ? tags.stream().map(tag -> new PostKeyword(this, tag, 1L)).collect(Collectors.toList())
                : new ArrayList<>();
        this.user = user;
    }

    // update 메서드
    public void update(String title, String content, Category category, Difficulty difficulty, List<Tag> tags, boolean isPublic) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.difficulty = difficulty;
        if (tags != null) {
            for (Tag tag : tags) {
                this.postKeywords.add(new PostKeyword(this, tag, 1L));
            }
        }
        this.isPublic = isPublic;
    }

    public PostResponse toDto() {
        return new PostResponse(this);
    }

    public Long getId() {
        return postId;  // postId를 반환
    }

    public String getTagsAsString() {
        return postKeywords.stream()
                .map(pk -> pk.getTag().getName())
                .collect(Collectors.joining(", "));
    }

    public List<Tag> getTags() {
        return postKeywords.stream()
                .map(PostKeyword::getTag) // Assuming PostKeyword has a 'getTag()' method
                .collect(Collectors.toList());
    }

}
