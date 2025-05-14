package com.estsoft.ormi_p2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddPostRequest {
    private String title;
    private String content;
    private String category;
    private String difficulty;
    private List<String> tags;
    private List<String> imageUrl;
    private String representImageUrl;

}