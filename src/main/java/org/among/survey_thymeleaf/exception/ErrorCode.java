package org.among.survey_thymeleaf.exception;

public enum ErrorCode {
    USER_NOT_FOUND_EXCEPTION("사용자를 찾을 수 없습니다."),
    BAD_CREDENTIALS_EXCEPTION("비밀번호가 맞지 않습니다."),
    USER_DUPLICATED_EXCEPTION("이미 존재하는 사용자 ID입니다."),
    PERMISSION_DENIED_EXCEPTION("해당 페이지에 대한 권한이 없습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
