package com.pesquisa.filmes.controller;

import com.pesquisa.filmes.dto.FilmeDTO;
import com.pesquisa.filmes.service.FilmeWebService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filmes")
@CrossOrigin(origins = "*")
public class FilmeController {

    private final FilmeWebService filmeWebService;

    public FilmeController(FilmeWebService filmeWebService) {
        this.filmeWebService = filmeWebService;
    }

    /**
     * Endpoint para pesquisa global de filmes por título, termo ou palavra-chave
     * Exemplo: GET /api/filmes/pesquisar?termo=Matrix
     */
    @GetMapping("/pesquisar")
    public ResponseEntity<List<FilmeDTO>> pesquisarFilmes(@RequestParam(required = false) String termo) {
        List<FilmeDTO> resultados = filmeWebService.pesquisarFilmes(termo);
        return ResponseEntity.ok(resultados);
    }

    /**
     * Endpoint para buscar filmes em alta / populares
     * Exemplo: GET /api/filmes/populares
     */
    @GetMapping("/populares")
    public ResponseEntity<List<FilmeDTO>> buscarPopulares() {
        List<FilmeDTO> populares = filmeWebService.buscarFilmesPopulares();
        return ResponseEntity.ok(populares);
    }

    /**
     * Endpoint para filtros rápidos (ex: avaliados, scifi, terror)
     * Exemplo: GET /api/filmes/filtro?tipo=avaliados
     */
    @GetMapping("/filtro")
    public ResponseEntity<List<FilmeDTO>> buscarPorFiltro(@RequestParam String tipo) {
        List<FilmeDTO> filmes = filmeWebService.buscarPorFiltroRapido(tipo);
        return ResponseEntity.ok(filmes);
    }
}
