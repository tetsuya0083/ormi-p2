package com.estsoft.ormi_p2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;

    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNum;

}
