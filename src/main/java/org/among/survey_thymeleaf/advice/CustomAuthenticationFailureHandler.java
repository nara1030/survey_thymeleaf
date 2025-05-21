package org.among.survey_thymeleaf.advice;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * 시큐리티 발생 에러 처리 핸들러
 * (시큐리티 필터 발생 예외는 DispatcherServlet까지 도달하지 않기에 @ControllerAdivce에서 안 잡힘)
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        request.setAttribute("errorMessage", exception.getMessage());
//        request.setAttribute("pageScript", "/js/error.js");
//        request.getRequestDispatcher("/error").forward(request, response); // 에러 페이지 포워드

        String errorMessage = URLEncoder.encode(exception.getMessage(), "UTF-8");
        String pageScript = URLEncoder.encode("/js/error.js", "UTF-8");
//        String redirectUrl = "/error?errorMessage=" + errorMessage + "&pageScript=" + pageScript;
//        setDefaultFailureUrl(redirectUrl);

        String redirectUrl = UriComponentsBuilder.fromPath("/security-error")
                        .queryParam("errorMessage", errorMessage)
                        .queryParam("pageScript", pageScript)
                        .build(true) // 자동 UTF-8 인코딩(쿼리 파라미터 디코딩 수행)
                        .toUriString();
        response.sendRedirect(redirectUrl);
    }
}
