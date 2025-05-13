package com.estsoft.ormi_p2.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImageId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = true)
    private boolean representImageYn;

    @Column(name = "image_url")
    private String imageUrl;

    public PostImage(String url, Post post) {
    }

    public static PostImage of(Post post, String imageUrl, boolean isRepresentative) {
        return PostImage.builder()
                .post(post)
                .imageUrl(imageUrl)
                .representImageYn(isRepresentative)
                .build();
    }

    public PostImage(Post post, String imageUrl, boolean representImageYn) {
        this.post = post;
        this.imageUrl = imageUrl;
        this.representImageYn = representImageYn;
    }

    public List<PostImage> getUrl() {
        return List.of();
    }
}
