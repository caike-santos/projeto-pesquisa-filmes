package com.pesquisa.filmes.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para receber os dados do formulário de cadastro de usuário.
 */
public class UsuarioCadastroDTO {

    @NotBlank(message = "O nome completo é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Forneça um e-mail válido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    private LocalDate nascimento;

    @Min(value = 10, message = "A idade mínima permitida é 10 anos")
    @Max(value = 120, message = "Idade inválida")
    private Integer idade;

    // Preferências de Conteúdo
    private List<String> formato = new ArrayList<>();
    private List<String> mood = new ArrayList<>();
    private String frequencia;
    private String generoFavorito;
    private List<String> subgenero = new ArrayList<>();
    private Integer pesoTecnologia = 5;
    private Integer pesoEmocao = 5;
    private Integer pesoComplexidade = 5;
    private String temaPesquisa;

    // Customização do Perfil
    private String corPerfil = "#e50914";
    private String avatar;
    private String bio;
    private String origemCadastro = "formulario_web";

    public UsuarioCadastroDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public List<String> getFormato() {
        return formato;
    }

    public void setFormato(List<String> formato) {
        this.formato = formato;
    }

    public List<String> getMood() {
        return mood;
    }

    public void setMood(List<String> mood) {
        this.mood = mood;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public String getGeneroFavorito() {
        return generoFavorito;
    }

    public void setGeneroFavorito(String generoFavorito) {
        this.generoFavorito = generoFavorito;
    }

    public List<String> getSubgenero() {
        return subgenero;
    }

    public void setSubgenero(List<String> subgenero) {
        this.subgenero = subgenero;
    }

    public Integer getPesoTecnologia() {
        return pesoTecnologia;
    }

    public void setPesoTecnologia(Integer pesoTecnologia) {
        this.pesoTecnologia = pesoTecnologia;
    }

    public Integer getPesoEmocao() {
        return pesoEmocao;
    }

    public void setPesoEmocao(Integer pesoEmocao) {
        this.pesoEmocao = pesoEmocao;
    }

    public Integer getPesoComplexidade() {
        return pesoComplexidade;
    }

    public void setPesoComplexidade(Integer pesoComplexidade) {
        this.pesoComplexidade = pesoComplexidade;
    }

    public String getTemaPesquisa() {
        return temaPesquisa;
    }

    public void setTemaPesquisa(String temaPesquisa) {
        this.temaPesquisa = temaPesquisa;
    }

    public String getCorPerfil() {
        return corPerfil;
    }

    public void setCorPerfil(String corPerfil) {
        this.corPerfil = corPerfil;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getOrigemCadastro() {
        return origemCadastro;
    }

    public void setOrigemCadastro(String origemCadastro) {
        this.origemCadastro = origemCadastro;
    }
}
