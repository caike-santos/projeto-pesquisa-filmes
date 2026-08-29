package com.pesquisa.filmes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade que representa o Usuário e seu perfil cinematográfico completo.
 * Mapeia todos os dados do formulário de cadastro e preferências de recomendação.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================================
    // 1. DADOS PESSOAIS
    // ==========================================

    @NotBlank(message = "O nome completo é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Forneça um endereço de e-mail válido")
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Column(nullable = false)
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Past(message = "A data de nascimento deve ser uma data passada")
    @Column(name = "data_nascimento")
    private LocalDate nascimento;

    @Min(value = 10, message = "A idade mínima é 10 anos")
    @Max(value = 120, message = "Idade inválida")
    private Integer idade;

    // ==========================================
    // 2. PREFERÊNCIAS DE CONTEÚDO (PARA RECOMENDAÇÕES)
    // ==========================================

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_formatos", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "formato")
    private List<String> formato = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_moods", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "mood")
    private List<String> mood = new ArrayList<>();

    @Column(name = "frequencia_maratona", length = 30)
    private String frequencia;

    @Column(name = "genero_favorito", length = 50)
    private String generoFavorito;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_subgeneros", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "subgenero")
    private List<String> subgenero = new ArrayList<>();

    @Min(1)
    @Max(10)
    @Column(name = "peso_tecnologia")
    private Integer pesoTecnologia = 5;

    @Min(1)
    @Max(10)
    @Column(name = "peso_emocao")
    private Integer pesoEmocao = 5;

    @Min(1)
    @Max(10)
    @Column(name = "peso_complexidade")
    private Integer pesoComplexidade = 5;

    @Column(name = "tema_pesquisa", length = 100)
    private String temaPesquisa;

    // ==========================================
    // 3. CUSTOMIZAÇÃO DO PERFIL
    // ==========================================

    @Column(name = "cor_perfil", length = 10)
    private String corPerfil = "#e50914";

    @Column(name = "avatar_url", length = 255)
    private String avatar;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "origem_cadastro", length = 50)
    private String origemCadastro = "formulario_web";

    // ==========================================
    // METADADOS E AUDITORIA
    // ==========================================

    @CreationTimestamp
    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    // Construtores
    public Usuario() {
    }

    public Usuario(Long id, String nome, String email, String senha, LocalDate nascimento, Integer idade,
                   List<String> formato, List<String> mood, String frequencia, String generoFavorito,
                   List<String> subgenero, Integer pesoTecnologia, Integer pesoEmocao, Integer pesoComplexidade,
                   String temaPesquisa, String corPerfil, String avatar, String bio, String origemCadastro,
                   LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.nascimento = nascimento;
        this.idade = idade;
        this.formato = formato != null ? formato : new ArrayList<>();
        this.mood = mood != null ? mood : new ArrayList<>();
        this.frequencia = frequencia;
        this.generoFavorito = generoFavorito;
        this.subgenero = subgenero != null ? subgenero : new ArrayList<>();
        this.pesoTecnologia = pesoTecnologia != null ? pesoTecnologia : 5;
        this.pesoEmocao = pesoEmocao != null ? pesoEmocao : 5;
        this.pesoComplexidade = pesoComplexidade != null ? pesoComplexidade : 5;
        this.temaPesquisa = temaPesquisa;
        this.corPerfil = corPerfil != null ? corPerfil : "#e50914";
        this.avatar = avatar;
        this.bio = bio;
        this.origemCadastro = origemCadastro != null ? origemCadastro : "formulario_web";
        this.dataCadastro = dataCadastro;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
