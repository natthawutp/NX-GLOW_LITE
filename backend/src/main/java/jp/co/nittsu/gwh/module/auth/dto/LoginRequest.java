package jp.co.nittsu.gwh.module.auth.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String locale;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }
}
