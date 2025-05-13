package com.estsoft.ormi_p2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeResponse {
    private boolean isLiked;
    private long likesCount;

    public LikeResponse(boolean isLiked, long likesCount) {
        this.isLiked = isLiked;
        this.likesCount = likesCount;
    }
}
