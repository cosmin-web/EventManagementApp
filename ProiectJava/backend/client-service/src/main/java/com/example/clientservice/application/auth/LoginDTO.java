package com.example.clientservice.application.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    @Email(message = "Emailul este invalid")
    @NotBlank(message = "Emailul este obligatoriu")
    private String email;

    @NotBlank(message = "Parola este obligatorie")
    private String parola;

    public LoginDTO() {
    }

    public LoginDTO(String email, String parola) {
        this.email = email;
        this.parola = parola;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }
    public void setParola(String parola) {
        this.parola = parola;
    }
}
