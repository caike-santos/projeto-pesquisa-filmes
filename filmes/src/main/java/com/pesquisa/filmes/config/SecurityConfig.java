package com.pesquisa.filmes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuração de Segurança e CORS do Spring Boot.
 * Permite requisições do GitHub Pages (https://caike-santos.github.io),
 * Live Server (portas 5500, 5501, etc.), localhost e clientes externos.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * Bean para criptografia de senhas usando o algoritmo BCrypt com Salt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuração das regras de acesso HTTP e integração CORS no Spring Security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Habilita o CORS no pipeline do Spring Security
            .cors(Customizer.withDefaults())
            // Desabilita CSRF para permitir requisições REST/JSON e formulários externos
            .csrf(AbstractHttpConfigurer::disable)
            // Libera todas as rotas públicas (cadastro, busca, recomendações e estáticos)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }

    /**
     * Configuração global de CORS para o Spring Security e REST APIs.
     * Suporta explicitamente:
     * - GitHub Pages: https://caike-santos.github.io
     * - Live Server: http://127.0.0.1:5500, http://localhost:5500
     * - Qualquer porta local de desenvolvimento.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowedOriginPatterns(List.of(
                "https://caike-santos.github.io*",
                "https://*.github.io*",
                "http://localhost:*",
                "http://127.0.0.1:*",
                "*"
        ));
        
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        config.setAllowCredentials(false);
        config.setMaxAge(3600L); // Cache pre-flight por 1 hora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Configuração adicional de CORS para o Spring MVC
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "https://caike-santos.github.io*",
                        "https://*.github.io*",
                        "http://localhost:*",
                        "http://127.0.0.1:*",
                        "*"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
