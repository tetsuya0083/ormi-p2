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
    private String category;  // enum 값 (예: "요리 수다방")
    private String difficulty; // enum 값 (예: "상")
    private List<String> tags;
    private List<String> imageUrl; // 이미지 URL 리스트
    private String representImageUrl; // 대표 이미지 1개

}