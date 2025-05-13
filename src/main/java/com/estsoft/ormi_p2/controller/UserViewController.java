package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.CategoryPostCountDto;
import com.estsoft.ormi_p2.service.PostCountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserViewController {
    private final PostCountService postCountService;

    public UserViewController(PostCountService postCountService) {
        this.postCountService = postCountService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);

        List<CategoryPostCountDto> postCounts = postCountService.getPostCountsByCategory(user.getUserId());
        model.addAttribute("postCounts", postCounts);

        Long totalPostCount = postCounts.stream()
                .mapToLong(CategoryPostCountDto::getPostCount)
                .sum();

        model.addAttribute("totalPostCount", totalPostCount);
        return "profile";
    }

    @GetMapping("/modifyprofile")
    public String modifyProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);

        return "modifyprofile";
    }

}
