package com.estsoft.ormi_p2.dto;

import com.estsoft.ormi_p2.domain.Difficulty;
import com.estsoft.ormi_p2.domain.Post;
import com.estsoft.ormi_p2.domain.PostImage;
import com.estsoft.ormi_p2.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String difficulty;
    private List<String> tags;
    private Boolean isPublic;
    private List<String> imageUrl;
    private String representImageUrl;

    public UpdatePostRequest(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory().name(); // enum → String
        this.difficulty = post.getDifficulty() != null ? post.getDifficulty().name() : null;

        this.tags = post.getTags() != null
                ? post.getTags().stream()
                .map(Tag::getName)
                .toList()
                : null;

        this.imageUrl = post.getImages() != null
                ? post.getImages().stream()
                .map(PostImage::getImageUrl)
                .toList()
                : null;

        this.representImageUrl = post.getImages() != null && !post.getImages().isEmpty()
                ? post.getImages().get(0).getImageUrl()
                : null;
    }
}
