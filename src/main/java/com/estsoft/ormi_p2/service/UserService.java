package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.AddUserRequest;
import com.estsoft.ormi_p2.dto.ModifyUserRequest;
import com.estsoft.ormi_p2.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void signUp(AddUserRequest request, String profileImageUrl) {
        if(!NicknameValidator.isValid(request.getNickname())) {
            throw new InvalidUserDataException("닉네임은 한글 최대 6자, 영문 최대 12자이며 특수문자/공백은 사용할 수 없습니다.");
        }

        try {
            userRepository.save(
                    new User(request.getLoginId(), encoder.encode(request.getPassword()),
                            request.getNickname(), request.getPhoneNum(), 1,
                            request.getEmail(), profileImageUrl,
                            User.Role.USER, User.Level.LEVEL_01
                    ));
        } catch (DataIntegrityViolationException e) {
            log.error("데이터 무결성 위반: {}", e.getMessage());
            throw new InvalidUserDataException("필수값이 누락되었거나 중복된 값이 있습니다.");
        }
    }

    public User modifyUser(ModifyUserRequest request, Long userId) {
        if(!NicknameValidator.isValid(request.getNickname())) {
            throw new InvalidUserDataException("닉네임은 한글 최대 6자, 영문 최대 12자이며 특수문자/공백은 사용할 수 없습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 닉네임 수정
        user.setNickname(request.getNickname());

        // 프로필 이미지 수정
        String profileImageUrl = request.getProfileImageUrl();
        if(profileImageUrl != null && !profileImageUrl.isEmpty()) {
            user.setProfileImageUrl(profileImageUrl);
        }

        userRepository.save(user);

        return user;
    }

    // 현재 로그인한 사용자 정보 가져오기
    public User getCurrentUser() {
        // SecurityContext에서 로그인한 사용자 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName(); // 로그인한 사용자 ID

        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("로그인된 사용자가 없습니다."));
    }

}