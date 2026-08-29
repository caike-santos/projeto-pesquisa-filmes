package com.pesquisa.filmes.service;

import com.pesquisa.filmes.dto.FilmeDTO;
import com.pesquisa.filmes.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Web Service para integração com APIs externas de filmes (como The Movie Database - TMDB / OMDb)
 * e geração de recomendações personalizadas com base no perfil do usuário.
 */
@Service
public class FilmeWebService {

    private static final Logger log = LoggerFactory.getLogger(FilmeWebService.class);

    @Value("${tmdb.api.key:}")
    private String apiKey;

    @Value("${tmdb.api.base-url:https://api.themoviedb.org/3}")
    private String apiBaseUrl;

    private final RestClient restClient;

    public FilmeWebService() {
        this.restClient = RestClient.builder().build();
    }

    /**
     * Busca recomendações personalizadas de filmes para o usuário com base em seu perfil.
     */
    public List<FilmeDTO> gerarRecomendacoesParaUsuario(Usuario usuario) {
        log.info("Gerando recomendações personalizadas para o usuário: {}", usuario.getNome());

        // Se a chave da API TMDB estiver configurada, consome o Web Service real
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            try {
                return consultarWebServiceTMDB(usuario);
            } catch (Exception e) {
                log.warn("Falha ao consultar TMDB Web Service ({}). Usando catálogo inteligente local.", e.getMessage());
            }
        }

        // Catálogo inteligente de fallback baseado nas preferências do cadastro
        return gerarRecomendacoesCatalogoLocal(usuario);
    }

    /**
     * Consulta o Web Service do TMDB (The Movie Database) via REST
     */
    private List<FilmeDTO> consultarWebServiceTMDB(Usuario usuario) {
        String genreId = mapearGeneroParaTMDB(usuario.getGeneroFavorito());

        String uri = String.format("%s/discover/movie?api_key=%s&language=pt-BR&sort_by=vote_average.desc&vote_count.gte=300%s",
                apiBaseUrl, apiKey, (genreId != null ? "&with_genres=" + genreId : ""));

        Map<?, ?> response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(Map.class);

        List<FilmeDTO> filmes = new ArrayList<>();
        if (response != null && response.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            for (Map<String, Object> item : results) {
                filmes.add(FilmeDTO.builder()
                        .id(((Number) item.get("id")).longValue())
                        .titulo((String) item.get("title"))
                        .tituloOriginal((String) item.get("original_title"))
                        .sinopse((String) item.get("overview"))
                        .posterUrl(item.get("poster_path") != null ? "https://image.tmdb.org/t/p/w500" + item.get("poster_path") : null)
                        .backdropUrl(item.get("backdrop_path") != null ? "https://image.tmdb.org/t/p/w1280" + item.get("backdrop_path") : null)
                        .dataLancamento((String) item.get("release_date"))
                        .notaMedia(item.get("vote_average") != null ? ((Number) item.get("vote_average")).doubleValue() : null)
                        .motivoRecomendacao("Recomendado com base no seu gênero favorito (" + usuario.getGeneroFavorito() + ")")
                        .build());
            }
        }
        return filmes;
    }

    /**
     * Mapeia os códigos do formulário para IDs numéricos oficiais do TMDB
     */
    private String mapearGeneroParaTMDB(String genero) {
        if (genero == null) return null;
        return switch (genero.toLowerCase()) {
            case "action" -> "28";
            case "aventura" -> "12";
            case "animacao" -> "16";
            case "comedy", "romcom" -> "35";
            case "crime", "noir" -> "80";
            case "documentarios" -> "99";
            case "drama", "biografia" -> "18";
            case "familia" -> "10751";
            case "fantasia" -> "14";
            case "guerra" -> "10752";
            case "horror" -> "27";
            case "misterio" -> "9648";
            case "romance" -> "10749";
            case "sci_fi", "cyberpunk" -> "878";
            case "thriller" -> "53";
            default -> null;
        };
    }

    /**
     * Catálogo local inteligente para fornecer sugestões precisas mesmo sem chave externa
     */
    private List<FilmeDTO> gerarRecomendacoesCatalogoLocal(Usuario usuario) {
        List<FilmeDTO> catalogo = obterCatalogoCompleto();

        String generoFav = usuario.getGeneroFavorito() != null ? usuario.getGeneroFavorito().toLowerCase() : "";
        List<String> subgeneros = usuario.getSubgenero() != null ? usuario.getSubgenero() : Collections.emptyList();
        String tema = usuario.getTemaPesquisa() != null ? usuario.getTemaPesquisa().toLowerCase() : "";
        int pesoTech = usuario.getPesoTecnologia() != null ? usuario.getPesoTecnologia() : 5;

        return catalogo.stream()
                .sorted((f1, f2) -> {
                    int score1 = calcularScore(f1, generoFav, subgeneros, tema, pesoTech);
                    int score2 = calcularScore(f2, generoFav, subgeneros, tema, pesoTech);
                    return Integer.compare(score2, score1);
                })
                .limit(8)
                .collect(Collectors.toList());
    }

    private int calcularScore(FilmeDTO filme, String generoFav, List<String> subgeneros, String tema, int pesoTech) {
        int score = 0;
        String fullInfo = (filme.getTitulo() + " " + filme.getSinopse() + " " + String.join(" ", filme.getGeneros())).toLowerCase();

        if (!generoFav.isEmpty() && fullInfo.contains(generoFav)) score += 30;
        if (!tema.isEmpty() && fullInfo.contains(tema)) score += 40;
        for (String sub : subgeneros) {
            if (fullInfo.contains(sub.toLowerCase())) score += 20;
        }
        if (pesoTech >= 7 && (fullInfo.contains("futuro") || fullInfo.contains("ia") || fullInfo.contains("tecnologia") || fullInfo.contains("cyberpunk"))) {
            score += 25;
        }
        return score;
    }

    private List<FilmeDTO> obterCatalogoCompleto() {
        return List.of(
                FilmeDTO.builder()
                        .id(1L)
                        .titulo("Blade Runner 2049")
                        .tituloOriginal("Blade Runner 2049")
                        .sinopse("Trinta anos após os acontecimentos do primeiro filme, um novo Blade Runner descobre um segredo enterrado há muito tempo que ameaça mergulhar o que resta da sociedade no caos.")
                        .dataLancamento("2017-10-06")
                        .notaMedia(8.0)
                        .generos(List.of("Ficção Científica", "Cyberpunk", "Mistério"))
                        .posterUrl("https://images.unsplash.com/photo-1534447677768-be436bb09401?w=500&auto=format&fit=crop&q=60")
                        .motivoRecomendacao("Combina perfeitamente com seu alto interesse em Cyberpunk, Tecnologia e Enredos Complexos.")
                        .build(),

                FilmeDTO.builder()
                        .id(2L)
                        .titulo("Interestelar")
                        .tituloOriginal("Interstellar")
                        .sinopse("Um grupo de astronautas viaja através de um buraco de minhoca no espaço à procura de um novo lar para a humanidade.")
                        .dataLancamento("2014-11-07")
                        .notaMedia(8.7)
                        .generos(List.of("Ficção Científica", "Drama", "Aventura"))
                        .posterUrl("https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=500&auto=format&fit=crop&q=60")
                        .motivoRecomendacao("Ideal para fãs de exploração espacial, ciência e narrativas altamente emocionantes.")
                        .build(),

                FilmeDTO.builder()
                        .id(3L)
                        .titulo("A Origem")
                        .tituloOriginal("Inception")
                        .sinopse("Um ladrão talentoso na arte perigosa da extração ganha uma chance de redenção se conseguir realizar o oposto: a inserção de uma ideia.")
                        .dataLancamento("2010-07-16")
                        .notaMedia(8.8)
                        .generos(List.of("Ação", "Suspense Psicológico", "Ficção Científica"))
                        .posterUrl("https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=500&auto=format&fit=crop&q=60")
                        .motivoRecomendacao("Trama de altíssima complexidade psicológica e visualmente inovadora.")
                        .build(),

                FilmeDTO.builder()
                        .id(4L)
                        .titulo("Duna: Parte 2")
                        .tituloOriginal("Dune: Part Two")
                        .sinopse("Paul Atreides se une a Chani e aos Fremen enquanto busca vingança contra os conspiradores que destruíram sua família.")
                        .dataLancamento("2024-03-01")
                        .notaMedia(8.6)
                        .generos(List.of("Ficção Científica", "Aventura", "Fantasia"))
                        .posterUrl("https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=500&auto=format&fit=crop&q=60")
                        .motivoRecomendacao("Ótima escolha para quem busca Space Opera épica e construção de mundo profunda.")
                        .build(),

                FilmeDTO.builder()
                        .id(5L)
                        .titulo("Arcane")
                        .tituloOriginal("Arcane")
                        .sinopse("Em meio ao conflito entre as cidades-gêmeas de Piltover e Zaun, duas irmãs lutam em lados opostos de uma guerra entre tecnologias mágicas e convicções incompatíveis.")
                        .dataLancamento("2021-11-06")
                        .notaMedia(9.0)
                        .generos(List.of("Animação", "Ação", "Steampunk", "Drama"))
                        .posterUrl("https://images.unsplash.com/photo-1578632767115-351597cf2477?w=500&auto=format&fit=crop&q=60")
                        .motivoRecomendacao("Recomendação principal para quem curte Animação, Steampunk e narrativas densas.")
                        .build(),

                FilmeDTO.builder()
                        .id(6L)
                        .titulo("Oppenheimer")
                        .tituloOriginal("Oppenheimer")
                        .sinopse("A história do cientista americano J. Robert Oppenheimer e seu papel no desenvolvimento da bomba atômica durante a Segunda Guerra Mundial.")
                        .dataLancamento("2023-07-20")
                        .notaMedia(8.9)
                        .generos(List.of("Drama", "Biografia", "Histórico"))
                        .posterUrl("https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=500&auto=format&fit=crop&q=60")
                        .motivoRecomendacao("Baseado em fatos reais com forte tensão psicológica e debate ético.")
                        .build()
        );
    }
}
