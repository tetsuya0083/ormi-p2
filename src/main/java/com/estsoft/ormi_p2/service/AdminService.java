package com.estsoft.ormi_p2.service;

import com.estsoft.ormi_p2.domain.User.Role;
import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.AdminPromoteDto;
import com.estsoft.ormi_p2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public User promoteToAdmin(AdminPromoteDto dto) {
        User user = userRepository.findByLoginId(dto.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        if (!user.getEmail().equals(dto.getEmail())) {
            throw new IllegalArgumentException("이메일이 일치하지 않습니다.");
        }

        if (!user.getPhoneNum().equals(dto.getPhoneNum())) {
            throw new IllegalArgumentException("전화번호가 일치하지 않습니다.");
        }

        user.setRole(Role.ADMIN);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> searchUsers(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return userRepository.findByNicknameContaining(keyword, pageable);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }












}
