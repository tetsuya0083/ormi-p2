package com.estsoft.ormi_p2.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    public enum Role {
        USER, ADMIN
    }

    @Column(nullable = false)
    private String phoneNum;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column
    private Level userLevel;

    public enum Level {
        LEVEL_01("텅빈 냉장고 요정"),
        LEVEL_02("계란을 접수한 자"),
        LEVEL_03("냉장고 탐험가"),
        LEVEL_04("냉털 보조 쉐프"),
        LEVEL_05("냉털 지배자");

        private final String displayName;

        Level(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Column(nullable = false)
    private int loginCount;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private int points;

    @Enumerated(EnumType.STRING)
    @Column
    private UserStatus status;

    public enum UserStatus {
        ACTIVE("정상 회원"),
        SUSPENDED_3_DAYS("3일 정지"),
        SUSPENDED_7_DAYS("7일 정지"),
        PERMANENT_BAN("영구 정지"),
        WITHDRAWN("탈퇴");

        private final String displayName;

        UserStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Column
    private int postsCount;

    @Column
    private int commentsCount;

    @Column(insertable = false, updatable = false)
    private LocalDateTime recentlyLogin;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public User(String loginId, String password, String nickname,
                String phoneNum, int loginCount, String email,
                String profileImageUrl, Role role, Level level) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.loginCount = loginCount;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.userLevel = level;
    }


    @Override
    public Collection<? extends GrantedAuthority>  getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
