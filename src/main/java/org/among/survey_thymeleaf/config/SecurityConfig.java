package org.among.survey_thymeleaf.config;

import org.among.survey_thymeleaf.advice.CustomAuthenticationFailureHandler;
import org.among.survey_thymeleaf.exception.ErrorCode;
import org.among.survey_thymeleaf.exception.PermissionDeniedException;
import org.among.survey_thymeleaf.filter.LoginRetryFilter;
import org.among.survey_thymeleaf.security.CustomAuthenticationProvider;
import org.among.survey_thymeleaf.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomAuthenticationProvider customAuthenticationProvider;

    private final CustomUserDetailsService customUserDetailsService;

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider, CustomUserDetailsService customUserDetailsService, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager(http)); // 커스텀 AuthenticationManager 세팅

        http
                .addFilterBefore(new LoginRetryFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/login", "/signup").anonymous() // 미인증 사용자만 접근 가능
                        .requestMatchers("/", "/security-error", "/css/**", "/js/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/withdraw").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/register/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // 예외 발생시키지 않고 세팅해주는 것 같아서 리팩토링 필요
                .exceptionHandling(e -> e
                        .accessDeniedHandler((req, res, ex) -> {
                            String errorMessage = URLEncoder.encode(ErrorCode.PERMISSION_DENIED_EXCEPTION.getMessage(), "UTF-8");
                            String pageScript = URLEncoder.encode("/js/error.js", "UTF-8");
                            String redirectUrl = UriComponentsBuilder.fromPath("/security-error")
                                    .queryParam("errorMessage", errorMessage)
                                    .queryParam("pageScript", pageScript)
                                    .build(true) // 자동 UTF-8 인코딩(쿼리 파라미터 디코딩 수행)
                                    .toUriString();

                            res.sendRedirect(redirectUrl);
                        })
                )
                .formLogin(req -> req
                        .loginPage("/login") // 커스텀 로그인 페이지 설정
                        .loginProcessingUrl("/login") // 기본 경로지만 명시적 설정(기로그인된 경우 해당 요청 차단 필터 등록)
                        .defaultSuccessUrl("/", true)
                        .failureHandler(customAuthenticationFailureHandler)
                )
                .logout(req -> req
                        .logoutUrl("/logout") // 기본 경로지만 명시적 설정
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) // 세션 무효화
                        .clearAuthentication(true) // 인증 정보 삭제
                        .deleteCookies("JSESSIONID", "remember-me") // 세션 쿠키 삭제(선택사항)
                )
                .rememberMe(req -> req
                        .rememberMeParameter("remember-me") // form 내 체크박스 name
                        .rememberMeCookieName("remember-me")
                        .tokenValiditySeconds(60 * 60 * 24) // 하루 유효
                        .userDetailsService(customUserDetailsService) // AuthenticationManager에 등록한 AuthenticationProvider와 별개로 기억된 사용자를 복원할 때만 사용
                );

        return http.build();
    }

    /**
     * form login시 사용할 CustomAuthenticationProvider 등록
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenticationProvider)
                .build();
    }
}
