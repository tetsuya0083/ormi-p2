package com.estsoft.ormi_p2.handler;

import com.estsoft.ormi_p2.domain.User.Role;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.repository.UserRepository;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String loginId = authentication.getName();
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        if (user.getRole() == User.Role.ADMIN) {
            response.sendRedirect("/admin/members");
        } else {
            response.sendRedirect("/");
        }
    }
}
