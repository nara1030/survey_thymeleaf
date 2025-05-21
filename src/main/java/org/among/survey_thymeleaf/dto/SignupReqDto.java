package org.among.survey_thymeleaf.dto;

public class SignupReqDto {
    private String email;

    private String password;

    private String name;

    private String role;

//    public User convertToUser() {
//        User user = new User();
//        user.setId(this.email);
//        user.setPassword(this.password);
//        user.setName(this.name);
//        user.setRole(Role.from(this.role));
//
//        return user;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
