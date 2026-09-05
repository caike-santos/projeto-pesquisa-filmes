/**
 * Configuração Centralizada da API do CinePerfil
 */
const API_CONFIG = {
    // URL gerada pelo Cloudflare Tunnel no celular
    CLOUDFLARE_URL: 'https://remarkable-deck-poison-skins.trycloudflare.com',

    /**
     * Retorna a URL completa da API baseada no ambiente onde o frontend está rodando
     */
    getUrl(endpoint) {
        // 1. Se estiver rodando no GitHub Pages, conecta ao túnel do Cloudflare
        if (window.location.hostname.includes('github.io')) {
            return `${this.CLOUDFLARE_URL}${endpoint}`;
        }

        // 2. Se estiver empacotado direto no Spring Boot na porta 8080
        if (window.location.port === '8080') {
            return endpoint;
        }

        // 3. Se estiver no Live Server ou desenvolvimento local
        const host = window.location.hostname === '127.0.0.1' ? '127.0.0.1' : 'localhost';
        return `http://${host}:8080${endpoint}`;
    }
};

window.API_CONFIG = API_CONFIG;
