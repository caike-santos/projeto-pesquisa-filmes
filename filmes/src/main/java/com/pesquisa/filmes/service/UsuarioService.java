package com.pesquisa.filmes.service;

import com.pesquisa.filmes.dto.FilmeDTO;
import com.pesquisa.filmes.dto.UsuarioCadastroDTO;
import com.pesquisa.filmes.model.Usuario;
import com.pesquisa.filmes.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final FilmeWebService filmeWebService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, FilmeWebService filmeWebService,
                          org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.filmeWebService = filmeWebService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cadastra um novo usuário a partir dos dados do formulário
     */
    @Transactional
    public Usuario cadastrarUsuario(UsuarioCadastroDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com o e-mail: " + dto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        // Criptografa a senha usando BCrypt com Salt
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setNascimento(dto.getNascimento());
        usuario.setIdade(dto.getIdade());
        usuario.setFormato(dto.getFormato() != null ? dto.getFormato() : new ArrayList<>());
        usuario.setMood(dto.getMood() != null ? dto.getMood() : new ArrayList<>());
        usuario.setFrequencia(dto.getFrequencia());
        usuario.setGeneroFavorito(dto.getGeneroFavorito());
        usuario.setSubgenero(dto.getSubgenero() != null ? dto.getSubgenero() : new ArrayList<>());
        usuario.setPesoTecnologia(dto.getPesoTecnologia() != null ? dto.getPesoTecnologia() : 5);
        usuario.setPesoEmocao(dto.getPesoEmocao() != null ? dto.getPesoEmocao() : 5);
        usuario.setPesoComplexidade(dto.getPesoComplexidade() != null ? dto.getPesoComplexidade() : 5);
        usuario.setTemaPesquisa(dto.getTemaPesquisa());
        usuario.setCorPerfil(dto.getCorPerfil() != null ? dto.getCorPerfil() : "#e50914");
        usuario.setAvatar(dto.getAvatar());
        usuario.setBio(dto.getBio());
        usuario.setOrigemCadastro(dto.getOrigemCadastro() != null ? dto.getOrigemCadastro() : "formulario_web");

        Usuario salvo = usuarioRepository.save(usuario);
        log.info("Usuário cadastrado com sucesso! ID: {}, Email: {}", salvo.getId(), salvo.getEmail());
        return salvo;
    }

    /**
     * Obtém as recomendações de filmes personalizadas para um determinado usuário
     */
    @Transactional(readOnly = true)
    public List<FilmeDTO> obterRecomendacoesDoUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + usuarioId));

        return filmeWebService.gerarRecomendacoesParaUsuario(usuario);
    }

    /**
     * Lista todos os usuários cadastrados
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Autentica o usuário pelo e-mail e valida a senha criptografada via BCrypt
     */
    @Transactional(readOnly = true)
    public Usuario autenticar(String email, String senhaPura) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("E-mail não cadastrado no sistema."));

        if (!passwordEncoder.matches(senhaPura, usuario.getSenha())) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        log.info("Usuário '{}' autenticado com sucesso!", usuario.getNome());
        return usuario;
    }

    /**
     * Busca usuário por ID
     */
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));
    }
}
