package org.among.survey_thymeleaf.exception;

public class UserDuplicatedException  extends RuntimeException {
    public UserDuplicatedException(String message) {
        super(message);
    }
}
