package org.among.survey_thymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String main(Model model) {
        model.addAttribute("title", "메인 페이지");
        return "main"; // templates/main.html 로 이동
    }
}
