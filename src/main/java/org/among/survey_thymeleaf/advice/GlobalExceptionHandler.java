package org.among.survey_thymeleaf.advice;

//import org.among.survey_thymeleaf.exception.PermissionDeniedException;
//import org.among.survey_thymeleaf.exception.UserNotFoundException;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 시큐리티의 에러가 아닌 일반 빈(ex. 컨트롤러, 서비스) 에러를 여기서 처리
 */
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler({UserNotFoundException.class, PermissionDeniedException.class})
//    public String handleUserExceptions(RuntimeException e, Model model) {
//        model.addAttribute("errorMessage", e.getMessage());
//
//        // 이 페이지 전용 JS 경로를 설정
//        model.addAttribute("pageScript", "/resources/js/error.js");
//
//        return "error";
//    }
//}
