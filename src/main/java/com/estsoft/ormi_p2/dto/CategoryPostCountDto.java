package com.estsoft.ormi_p2.dto;

import com.estsoft.ormi_p2.domain.Category;
import lombok.Getter;

@Getter
public class CategoryPostCountDto {
    private Category category;
    private Long postCount;

    public CategoryPostCountDto(Category category, Long postCount) {
        this.category = category;
        this.postCount = postCount;
    }

}
