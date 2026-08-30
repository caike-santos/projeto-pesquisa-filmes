package com.pesquisa.filmes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para recebimento das credenciais de login.
 */
public class LoginDTO {

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Forneça um e-mail válido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    public LoginDTO() {
    }

    public LoginDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
