package com.pesquisa.filmes.controller;

import com.pesquisa.filmes.dto.FilmeDTO;
import com.pesquisa.filmes.dto.UsuarioCadastroDTO;
import com.pesquisa.filmes.model.Usuario;
import com.pesquisa.filmes.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint REST para login e autenticação de usuário (POST /api/usuarios/login)
     */
    @PostMapping("/api/usuarios/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody com.pesquisa.filmes.dto.LoginDTO dto) {
        try {
            Usuario usuarioAutenticado = usuarioService.autenticar(dto.getEmail(), dto.getSenha());
            List<FilmeDTO> recomendacoes = usuarioService.obterRecomendacoesDoUsuario(usuarioAutenticado.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "sucesso");
            response.put("mensagem", "Login realizado com sucesso! Bem-vindo(a) de volta.");
            response.put("usuario", usuarioAutenticado);
            response.put("recomendacoes", recomendacoes);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "erro");
            errorResponse.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * Endpoint REST para cadastro via JSON (POST /api/usuarios)
     */
    @PostMapping("/api/usuarios")
    public ResponseEntity<Map<String, Object>> cadastrarViaJson(@Valid @RequestBody UsuarioCadastroDTO dto) {
        Usuario usuarioSalvo = usuarioService.cadastrarUsuario(dto);
        List<FilmeDTO> recomendacoes = usuarioService.obterRecomendacoesDoUsuario(usuarioSalvo.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "sucesso");
        response.put("mensagem", "Perfil cinematográfico criado com sucesso!");
        response.put("usuario", usuarioSalvo);
        response.put("recomendacoes", recomendacoes);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para submissão direta do formulário HTML (POST /processar_cadastro)
     */
    @PostMapping("/processar_cadastro")
    public ResponseEntity<Map<String, Object>> processarFormularioCadastro(@Valid @ModelAttribute UsuarioCadastroDTO dto) {
        Usuario usuarioSalvo = usuarioService.cadastrarUsuario(dto);
        List<FilmeDTO> recomendacoes = usuarioService.obterRecomendacoesDoUsuario(usuarioSalvo.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "sucesso");
        response.put("mensagem", "Cadastro processado com sucesso!");
        response.put("usuario", usuarioSalvo);
        response.put("recomendacoes", recomendacoes);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retorna a lista de recomendações personalizadas para um usuário existente
     */
    @GetMapping("/api/usuarios/{id}/recomendacoes")
    public ResponseEntity<List<FilmeDTO>> obterRecomendacoes(@PathVariable Long id) {
        List<FilmeDTO> recomendacoes = usuarioService.obterRecomendacoesDoUsuario(id);
        return ResponseEntity.ok(recomendacoes);
    }

    /**
     * Lista todos os usuários
     */
    @GetMapping("/api/usuarios")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    /**
     * Busca usuário por ID
     */
    @GetMapping("/api/usuarios/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
}
