package com.estsoft.ormi_p2.dto;

import com.estsoft.ormi_p2.domain.Difficulty;
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
    private String title;
    private String content;
    private String category;
    private List<Tag> tags;
    private Difficulty difficulty;
    private Boolean isPublic;
}