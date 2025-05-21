package org.among.survey_thymeleaf.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 로그인 중복(외 회원가입 등도..) 시도 방지 필터
 * cf. 로그아웃의 경우 반대로..
 */
@Component
public class LoginRetryFilter extends GenericFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRetryFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ("/login".equals(request.getRequestURI())
                && "POST".equalsIgnoreCase(request.getMethod())
                && authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            LOGGER.info("중복 로그인 방지 필터 진입...");

            response.sendRedirect("/");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
