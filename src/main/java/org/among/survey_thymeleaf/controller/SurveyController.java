package org.among.survey_thymeleaf.controller;

import org.among.survey_thymeleaf.dto.ObjectiveReqDto;
import org.among.survey_thymeleaf.dto.SubjectiveReqDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SurveyController {
    @GetMapping("/register")
    public String register() {
        return "";
    }

    @PostMapping("/register/objective")
    public String register(ObjectiveReqDto objectiveReqDto) {
        return "";
    }

    @PostMapping("/register/subjective")
    public String register(SubjectiveReqDto subjectiveReqDto) {
        return "";
    }
}
