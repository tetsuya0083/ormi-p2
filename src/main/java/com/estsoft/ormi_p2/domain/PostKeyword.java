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
@Table(name = "post_keyword")
@Builder
public class PostKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_tag_id")
    private Long postTagId;

    @Column(name = "keyword_id", nullable = false)
    private Long keywordId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    // 생성자
    public PostKeyword(Post post, Tag tag, Long keywordId) {
        this.post = post;
        this.tag = tag;
        this.keywordId = keywordId;
    }
}
