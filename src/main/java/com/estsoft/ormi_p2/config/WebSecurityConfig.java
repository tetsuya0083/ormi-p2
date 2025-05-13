package com.estsoft.ormi_p2.config;

import com.estsoft.ormi_p2.handler.LoginSuccessHandler;
import com.estsoft.ormi_p2.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class WebSecurityConfig {
    private final UserDetailService userDetailService;
    private final LoginSuccessHandler loginSuccessHandler;

    public WebSecurityConfig(UserDetailService userDetailService, LoginSuccessHandler loginSuccessHandler) {
        this.userDetailService = userDetailService;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public WebSecurityCustomizer configure() {      // 1) 스프링 시큐리티 기능 비활성화
        return web -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**");
    }

//     2) 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth ->              // 인증, 인가 설정
                        auth.requestMatchers("/", "/login", "/signup", "/user", "/admin/promote",
                                        "/api/nickname-check").permitAll()
                                .requestMatchers("/admin/members" ).hasRole("ADMIN")  // ROLE_ADMIN
                                .anyRequest().authenticated())
                .formLogin(auth -> auth.loginPage("/login")     // 폼 기반 로그인 설정
                        .successHandler(loginSuccessHandler))
                .logout(auth -> auth.logoutSuccessUrl("/") // 로그아웃 설정
                        .invalidateHttpSession(true)
                        .clearAuthentication(true))
                .csrf(AbstractHttpConfigurer::disable);                  // csrf 비활성화
        return httpSecurity.build();
    }


    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}