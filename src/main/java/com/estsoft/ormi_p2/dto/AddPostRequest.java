package com.estsoft.ormi_p2.dto;

import com.estsoft.ormi_p2.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddPostRequest {
    private String title;
    private String content;
    private String category;
    private List<String> tags;  // Updated: tags as List<String>
    private Difficulty difficulty;
    private Boolean isPublic;
    private MultipartFile image;

    public Post toEntity() {

        List<Tag> tagList = processTags(tags);

        Post post = Post.builder()
                .title(title)
                .content(content)
                .category(Category.valueOf(category))
                .difficulty(difficulty)
                .isPublic(isPublic != null ? isPublic : true)
                .likesCount(0)
                .commentsCount(0)
                .build();

        List<PostKeyword> postKeywords = tagList.stream()
                .map(tag -> new PostKeyword(post, tag))
                .collect(Collectors.toList());

        post.setPostKeywords(postKeywords);
        return post;
    }

    private List<Tag> processTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }

        return tags.stream()
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .map(tag -> new Tag(tag))
                .collect(Collectors.toList());
    }

}