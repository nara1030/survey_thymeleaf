package org.among.survey_thymeleaf.service;

import org.among.survey_thymeleaf.dao.UserRepository;
import org.among.survey_thymeleaf.dto.SignupReqDto;
import org.among.survey_thymeleaf.exception.ErrorCode;
import org.among.survey_thymeleaf.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDetailsService userDetailsService = userRepository.getUserDetailsManager();
            return userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND_EXCEPTION.getMessage());
        }
    }

    /**
     * 사용자 등록(시큐리티 무관하나 맥락상 한 클래스 작성)
     */
    public void create(SignupReqDto signupReqDto) {
        UserDetails user = User.withUsername(signupReqDto.getEmail())
                .password(passwordEncoder.encode(signupReqDto.getPassword()))
                .roles(signupReqDto.getRole().toUpperCase()) // ADMIN, USER
                .build();

        userRepository.create(user);
    }
}
