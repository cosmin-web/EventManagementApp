package com.example.eventservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    private Integer id;

    @Email(message = "Emailul este invalid")
    @NotBlank(message = "Emailul este obligatoriu")
    private String email;

    @NotBlank(message = "Rolul este obligatoriu")
    private String rol;

    @NotBlank(message = "Parola este obligatorie")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String parola;

    public UserDTO() {}

    public UserDTO(Integer id, String email, String rol) {
        this.id = id;
        this.email = email;
        this.rol = rol;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getParola() { return parola; }
    public void setParola(String parola) { this.parola = parola; }
}
