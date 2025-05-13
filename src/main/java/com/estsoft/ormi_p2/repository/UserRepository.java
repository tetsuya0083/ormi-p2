package com.estsoft.ormi_p2.repository;

import com.estsoft.ormi_p2.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Page<User> findByNicknameContaining(String keyword, Pageable pageable);

}