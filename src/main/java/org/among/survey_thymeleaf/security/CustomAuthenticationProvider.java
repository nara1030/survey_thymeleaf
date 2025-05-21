package org.among.survey_thymeleaf.security;

import org.among.survey_thymeleaf.exception.ErrorCode;
import org.among.survey_thymeleaf.exception.UserNotFoundException;
import org.among.survey_thymeleaf.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService customUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // 1. 사용자 인증
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // 2. 비밀번호 검증
        Authentication auth;
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            // 비번 던질 필요 없다. 클라이언트에서 비번 접근 불가한지 확인 필요
            auth = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException(ErrorCode.BAD_CREDENTIALS_EXCEPTION.getMessage());
        }

        // 3. 사용자 정보 Context 저장(Provider 재정의한 경우에도 수동 저장 불필요)
//        SecurityContextHolder.getContext().setAuthentication(auth);

        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
