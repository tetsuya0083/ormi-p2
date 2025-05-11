package com.estsoft.ormi_p2.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @Column(nullable = false)
    private String imageUrl;

    public static PostImage of(Post post, String imageUrl, boolean isRepresentative) {
        return PostImage.builder()
                .post(post)
                .imageUrl(imageUrl)
                .representImageYn(isRepresentative)
                .build();
    }
}