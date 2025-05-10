package com.estsoft.ormi_p2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String nickname;
    private String loginId;
    private String email;
    private String password;
    private String phoneNum;
    private String profileImageUrl;
}
