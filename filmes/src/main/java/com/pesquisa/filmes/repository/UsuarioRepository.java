package com.pesquisa.filmes.repository;

import com.pesquisa.filmes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca usuário pelo e-mail
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica se já existe usuário cadastrado com determinado e-mail
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuários filtrando pelo gênero cinematográfico favorito
     */
    List<Usuario> findByGeneroFavorito(String generoFavorito);
}
