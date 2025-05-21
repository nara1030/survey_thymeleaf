package org.among.survey_thymeleaf.dao;

import org.among.survey_thymeleaf.exception.ErrorCode;
import org.among.survey_thymeleaf.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    /**
     * 테스트용 초기 유저
     */
    public UserRepository(PasswordEncoder passwordEncoder) {
        inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("abc@naver.com")
                .password(passwordEncoder.encode("1234"))
                .roles("ADMIN")
                .build()
        );
        inMemoryUserDetailsManager.createUser(User.withUsername("bcd@naver.com")
                .password(passwordEncoder.encode("4321"))
                .roles("USER")
                .build()
        );
    }

    public UserDetailsService getUserDetailsManager() {
        return inMemoryUserDetailsManager;
    }

    public void create(UserDetails user) {
        if (inMemoryUserDetailsManager.userExists(user.getUsername())) {
            throw new UserNotFoundException(ErrorCode.USER_DUPLICATED_EXCEPTION.getMessage());
        }

        inMemoryUserDetailsManager.createUser(user);
    }
}
