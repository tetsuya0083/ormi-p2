package com.estsoft.ormi_p2.controller;

import com.estsoft.ormi_p2.domain.User;
import com.estsoft.ormi_p2.dto.AdminPromoteDto;
import com.estsoft.ormi_p2.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/promote")
    public String showPromoteForm() {
        return "adminPromote"; // path: templates/adminPromote.html
    }

    @PostMapping("/promote")
    public String promote(@Valid @ModelAttribute AdminPromoteDto dto,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "입력값 오류");
            return "adminPromote";
        }

        try {
            User admin = adminService.promoteToAdmin(dto);
            model.addAttribute("message", admin.getLoginId() + "님이 관리자로 전환되었습니다.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "adminPromote";
    }

    @GetMapping("/members")
    public String viewMembers(Model model,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page) {
        Page<User> users = adminService.searchUsers(keyword, page); // 검색 및 페이징
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);

        return "adminMemberManage";
    }

    @GetMapping("/members/{userId}")
    public String viewUserDetail(@PathVariable Long userId, Model model) {
        User user = adminService.getUserById(userId);
        model.addAttribute("user", user);
        return "adminMemberDetail";
    }

    @PostMapping("/members/{userId}")
    public String updateUser(@PathVariable Long userId,
                             @RequestParam String nickname,
                             @RequestParam String userLevel,
                             @RequestParam String status,
                             @RequestParam int points,
                             RedirectAttributes redirectAttributes) {
        User user = adminService.getUserById(userId);
        user.setNickname(nickname);
        user.setUserLevel(User.Level.valueOf(userLevel));
        user.setStatus(User.UserStatus.valueOf(status));
        user.setPoints(points);

        adminService.updateUser(user); // save

        redirectAttributes.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
        return "redirect:/admin/members";
    }






}
