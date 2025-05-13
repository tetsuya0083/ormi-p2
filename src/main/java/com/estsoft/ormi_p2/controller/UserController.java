package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.AddUserRequest;
import com.estsoft.ormi_p2.dto.ModifyUserRequest;
import com.estsoft.ormi_p2.repository.UserRepository;
import com.estsoft.ormi_p2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Controller
public class UserController {
    private UserService userService;
    private UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/user")
    public ResponseEntity<String> signUp(@Valid @ModelAttribute AddUserRequest request,
                                         @RequestParam("profileImage") MultipartFile profileImage,
                                         BindingResult result)
            throws IOException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("입력값 오류");
        }

        String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
        Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
        Files.createDirectories(uploadDir);

        Path fullPath = uploadDir.resolve(filename);
        profileImage.transferTo(fullPath.toFile());


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

    @GetMapping("/api/nickname-check")
    @ResponseBody
    public Map<String, Boolean> checkNickname(@RequestParam String nickname) {
        boolean isAvailable = !userRepository.existsByNickname(nickname);
        return Map.of("available", isAvailable);
    }

    @PostMapping("/user/{userId}")
    public String modifyUser(@ModelAttribute ModifyUserRequest request,
                             @PathVariable Long userId,
                             @RequestParam("profileImage") MultipartFile profileImage)
            throws IOException {
        if(profileImage != null && !profileImage.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
            Files.createDirectories(uploadDir);

            Path fullPath = uploadDir.resolve(filename);
            profileImage.transferTo(fullPath.toFile());

            String imageUrl = "/images/" + filename;

            request.setProfileImageUrl(imageUrl);
        }

        User updatedUser = userService.modifyUser(request, userId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUser,
                authentication.getCredentials(),
                authentication.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);


        return "redirect:/profile";
    }
}