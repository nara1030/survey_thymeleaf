package org.among.survey_thymeleaf.controller;

import org.among.survey_thymeleaf.dto.SignupReqDto;
import org.among.survey_thymeleaf.service.CustomUserDetailsService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {
    private final CustomUserDetailsService customUserDetailsService;

    public JoinController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        // 시큐리티 필터 체인 설정
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
//            return "redirect:/";
//        }
        model.addAttribute("signupForm", new SignupReqDto());

        return "signup";
    }

    @PostMapping("/signup")
    public String signup(SignupReqDto signupReqDto) {
        customUserDetailsService.create(signupReqDto);

        return "redirect:/";
    }

    // 회원 탈퇴
    @PostMapping("/withdraw")
    public String withdraw(SignupReqDto signupReqDto) {

        return "";
    }
}
