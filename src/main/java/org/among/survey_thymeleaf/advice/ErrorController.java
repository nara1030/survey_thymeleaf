package org.among.survey_thymeleaf.advice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {
    @GetMapping("/security-error")
    public String errorPage(@RequestParam(required = false) String errorMessage, @RequestParam(required = false) String pageScript, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("pageScript", pageScript);

        return "error";
    }
}
