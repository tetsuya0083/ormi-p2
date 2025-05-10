package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.dto.AddUserRequest;
import com.estsoft.ormi_p2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<String> signUp(@ModelAttribute AddUserRequest request,
                                         @RequestParam("profileImage") MultipartFile profileImage)
    throws IOException {
        String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
        Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads"); // 현재 프로젝트 기준 절대 경로
        Files.createDirectories(uploadDir);

        Path fullPath = uploadDir.resolve(filename);  // uploads/파일명 전체 경로
        profileImage.transferTo(fullPath.toFile());   // 절대 경로 넘김!

        // 클라이언트가 접근할 수 있는 URL 생성
        String imageUrl = "/images/" + filename;

        userService.signUp(request, imageUrl);

        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/logoutPage")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);

        return "redirect:/login";
    }
}
