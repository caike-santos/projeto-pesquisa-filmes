package com.pesquisa.filmes.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO que representa um filme ou série retornado pelo Web Service e sugerido ao usuário.
 */
public class FilmeDTO {

    private Long id;
    private String titulo;
    private String tituloOriginal;
    private String sinopse;
    private String posterUrl;
    private String backdropUrl;
    private String dataLancamento;
    private Double notaMedia;
    private Integer totalVotos;
    private List<String> generos = new ArrayList<>();
    private String formato;
    private String motivoRecomendacao;

    public FilmeDTO() {
    }

    public FilmeDTO(Long id, String titulo, String tituloOriginal, String sinopse, String posterUrl,
                    String backdropUrl, String dataLancamento, Double notaMedia, Integer totalVotos,
                    List<String> generos, String formato, String motivoRecomendacao) {
        this.id = id;
        this.titulo = titulo;
        this.tituloOriginal = tituloOriginal;
        this.sinopse = sinopse;
        this.posterUrl = posterUrl;
        this.backdropUrl = backdropUrl;
        this.dataLancamento = dataLancamento;
        this.notaMedia = notaMedia;
        this.totalVotos = totalVotos;
        this.generos = generos != null ? generos : new ArrayList<>();
        this.formato = formato;
        this.motivoRecomendacao = motivoRecomendacao;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String titulo;
        private String tituloOriginal;
        private String sinopse;
        private String posterUrl;
        private String backdropUrl;
        private String dataLancamento;
        private Double notaMedia;
        private Integer totalVotos;
        private List<String> generos = new ArrayList<>();
        private String formato;
        private String motivoRecomendacao;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public Builder tituloOriginal(String tituloOriginal) {
            this.tituloOriginal = tituloOriginal;
            return this;
        }

        public Builder sinopse(String sinopse) {
            this.sinopse = sinopse;
            return this;
        }

        public Builder posterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
            return this;
        }

        public Builder backdropUrl(String backdropUrl) {
            this.backdropUrl = backdropUrl;
            return this;
        }

        public Builder dataLancamento(String dataLancamento) {
            this.dataLancamento = dataLancamento;
            return this;
        }

        public Builder notaMedia(Double notaMedia) {
            this.notaMedia = notaMedia;
            return this;
        }

        public Builder totalVotos(Integer totalVotos) {
            this.totalVotos = totalVotos;
            return this;
        }

        public Builder generos(List<String> generos) {
            this.generos = generos;
            return this;
        }

        public Builder formato(String formato) {
            this.formato = formato;
            return this;
        }

        public Builder motivoRecomendacao(String motivoRecomendacao) {
            this.motivoRecomendacao = motivoRecomendacao;
            return this;
        }

        public FilmeDTO build() {
            return new FilmeDTO(id, titulo, tituloOriginal, sinopse, posterUrl, backdropUrl, dataLancamento,
                    notaMedia, totalVotos, generos, formato, motivoRecomendacao);
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTituloOriginal() {
        return tituloOriginal;
    }

    public void setTituloOriginal(String tituloOriginal) {
        this.tituloOriginal = tituloOriginal;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Double getNotaMedia() {
        return notaMedia;
    }

    public void setNotaMedia(Double notaMedia) {
        this.notaMedia = notaMedia;
    }

    public Integer getTotalVotos() {
        return totalVotos;
    }

    public void setTotalVotos(Integer totalVotos) {
        this.totalVotos = totalVotos;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getMotivoRecomendacao() {
        return motivoRecomendacao;
    }

    public void setMotivoRecomendacao(String motivoRecomendacao) {
        this.motivoRecomendacao = motivoRecomendacao;
    }
}
