package com.estsoft.ormi_p2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyUserRequest {
    private String nickname;
    private String profileImageUrl;
}