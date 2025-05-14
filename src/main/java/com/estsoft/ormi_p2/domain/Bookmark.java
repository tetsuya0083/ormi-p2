// src/main/java/com/estsoft/ormi_p2/domain/Bookmark.java
package com.estsoft.ormi_p2.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmark")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "created_at",
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP",
            insertable = false,
            updatable = false)
    private LocalDateTime createdAt;

    /** 편의 팩토리 메서드 */
    public static Bookmark of(User user, Post post) {
        Bookmark b = new Bookmark();
        b.user = user;
        b.post = post;
        return b;
    }
}

