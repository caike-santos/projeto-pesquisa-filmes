# 🎬 CinePerfil - Plataforma de Pesquisa e Recomendação de Filmes

<div align="center">

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-BCrypt-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6+-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![CSS3](https://img.shields.io/badge/CSS3-Dark_Theme-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![Cloudflare](https://img.shields.io/badge/Cloudflare_Tunnel-P2P-F38020?style=for-the-badge&logo=cloudflare&logoColor=white)

<p align="center">
  Uma aplicação web fullstack para descoberta de filmes e séries com algoritmo de recomendações personalizadas, integração em tempo real com o <b>The Movie Database (TMDB)</b> e arquitetura escalável.
</p>

[🌐 Acessar Demonstração (GitHub Pages)](https://caike-santos.github.io/projeto-pesquisa-filmes/frontend/) • [📖 Documentação da API](#-documentação-da-api-rest) • [🚀 Como Executar](#-como-executar-o-projeto)

</div>

---

## 📌 Sumário
- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Como Executar o Projeto](#-como-executar-o-projeto)
  - [Configuração do Backend (Spring Boot + MySQL)](#1-configuração-do-backend-spring-boot--mysql)
  - [Execução do Frontend](#2-execução-do-frontend)
  - [Rodando o Servidor no Android (Termux)](#-servidor-portátil-no-android-via-termux)
- [Documentação da API REST](#-documentação-da-api-rest)
- [Segurança & Boas Práticas](#-segurança--boas-práticas)
- [Licença](#-licença)

---

## 🌟 Sobre o Projeto

O **CinePerfil** foi desenvolvido com o objetivo de oferecer uma experiência cinematográfica sob medida. Em vez de recomendações genéricas, o sistema traça um **perfil cinematográfico detalhado** de cada usuário no momento do cadastro (considerando humor atual, formatos favoritos, temas narrativos e pesos de complexidade, emoção e tecnologia).

A partir desses parâmetros, o backend processa os dados e cruza as preferências com o **Web Service oficial do TMDB (The Movie Database)**, entregando sugestões inteligentes, capas em alta definição, notas da crítica e sinopses completas.

---

## ✨ Funcionalidades Principais

- 👤 **Cadastro Cinematográfico Completo**:
  - Preferências de formatos (Longas, Séries, Minisséries, Animes, Curtas).
  - Seleção de Humor/Mood (Adrenalina, Reflexivo, Leve, Nostálgico, Tensão).
  - Gêneros e subgêneros organizados com optgroups (Cyberpunk, Space Opera, Noir, Slasher, etc.).
  - Sliders dinâmicos de peso (Nível de Tecnologia, Intensidade Emocional e Complexidade do Enredo).
  - Datalist com mais de 30 temas narrativos (ex: *Viagem no Tempo, IA e Futuro, Distopia, Conspiração*).
  - Personalização visual: cor de perfil personalizada, avatar e biografia.

- 🔒 **Autenticação & Segurança**:
  - Senhas criptografadas utilizando o algoritmo padrão da indústria **BCrypt com Salt**.
  - Ocultação automática de campos sensíveis nas respostas JSON (`WRITE_ONLY`).
  - Tela de login funcional e integrada com validação assíncrona.

- 🔍 **Pesquisa Global de Filmes**:
  - Busca em tempo real por título, ator ou diretor conectada à API do TMDB.
  - Filtros rápidos por chips: *🔥 Em Alta*, *⭐ Mais Bem Avaliados*, *🚀 Ficção Científica*, *👻 Terror & Suspense*.
  - Filtro instantâneo no frontend para refinar os resultados exibidos na grade.

- 🧠 **Motor Inteligente de Recomendações com Fallback**:
  - Recomendações geradas com base nas pontuações de relevância de cada perfil.
  - Modo Offline / Fallback inteligente com catálogo curado local caso a chave do TMDB não esteja configurada.

- 🎨 **Interface Moderna & Acessível**:
  - Design sofisticado em **Dark Mode sólido** (sem gradientes, com alto contraste e foco em usabilidade).
  - Variáveis CSS no `:root` para fácil manutenção e consistência de cores.
  - Totalmente responsivo para smartphones, tablets e desktops.

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17 / 21 / 25 (LTS)**
- **Spring Boot 3.3.4**:
  - *Spring Web MVC*: Construção dos controladores REST.
  - *Spring Data JPA / Hibernate*: Mapeamento objeto-relacional e persistência.
  - *Spring Security*: Algoritmo BCrypt para hash seguro de senhas.
  - *Bean Validation (Jakarta Validation)*: Validação de formulários e integridade de dados.
  - *RestClient*: Comunicação HTTP assíncrona com o TMDB Web Service.
- **MySQL 8 / MariaDB**: Banco de dados relacional.
- **dotenv-java**: Gerenciamento de variáveis de ambiente seguras.

### Frontend
- **HTML5 Semântico**
- **CSS3 Moderno** (Flexbox, CSS Grid, Custom Properties/Variáveis, Design Tokens)
- **JavaScript Vanilla (ES6+)** com `fetch` assíncrono e `sessionStorage`.

### DevOps, Hospedagem & Rede
- **GitHub Pages**: Hospedagem do Frontend.
- **Cloudflare Tunnels**: Exposição segura do backend para a internet via túnel HTTPS.
- **Termux (Android)**: Capacidade de rodar o servidor fullstack (Java + MySQL) em um smartphone como servidor de baixo consumo.

---

## 📂 Estrutura do Projeto

```
projeto-pesquisa-filmes/
├── .env.example                     # Modelo de variáveis de ambiente
├── .gitignore                       # Proteção de chaves e builds
├── README.md                        # Documentação do projeto
│
├── frontend/                        # Interface Web
│   ├── index.html                   # Formulário de cadastro cinematográfico
│   ├── login.html                   # Tela de login e autenticação
│   ├── home.html                    # Dashboard de recomendações e busca
│   ├── style.css                    # Folha de estilos Dark Mode unificada
│   ├── app.js                       # Script de envio e validação do cadastro
│   ├── login.js                     # Script de autenticação e sessão
│   └── home.js                      # Motor de busca e renderização de cards
│
└── filmes/                          # Backend Spring Boot
    ├── pom.xml                      # Dependências Maven do projeto
    ├── mvnw / mvnw.cmd              # Maven Wrapper
    └── src/
        └── main/
            ├── java/com/pesquisa/filmes/
            │   ├── FilmesApplication.java     # Classe principal com carregamento de .env
            │   ├── config/                    # Configurações de Segurança e CORS
            │   │   ├── SecurityConfig.java
            │   │   └── CorsAndPrivateNetworkFilter.java
            │   ├── controller/                # Endpoints REST
            │   │   ├── UsuarioController.java
            │   │   └── FilmeController.java
            │   ├── dto/                       # Data Transfer Objects
            │   │   ├── UsuarioCadastroDTO.java
            │   │   ├── LoginDTO.java
            │   │   └── FilmeDTO.java
            │   ├── model/                     # Entidades JPA
            │   │   └── Usuario.java
            │   ├── repository/                # Interfaces de acesso a dados
            │   │   └── UsuarioRepository.java
            │   └── service/                   # Regras de negócio e integração API
            │       ├── UsuarioService.java
            │       └── FilmeWebService.java
            └── resources/
                └── application.properties     # Configurações do MySQL, JPA e TMDB
```

---

## 🚀 Como Executar o Projeto

### 1. Configuração do Backend (Spring Boot + MySQL)

#### Pré-requisitos
- JDK 17 ou superior instalado.
- MySQL ou MariaDB instalado e em execução na porta `3306`.

#### Passo a passo
1. **Clone o repositório**:
   ```bash
   git clone https://github.com/caike-santos/projeto-pesquisa-filmes.git
   cd projeto-pesquisa-filmes
   ```

2. **Configure o arquivo `.env`**:
   Copie o arquivo de exemplo para criar o seu `.env`:
   ```bash
   cp filmes/.env.example filmes/.env
   ```
   Abra o arquivo `filmes/.env` e insira suas credenciais:
   ```env
   TMDB_API_KEY=sua_chave_do_tmdb_aqui
   TMDB_API_BASE_URL=https://api.themoviedb.org/3
   TMDB_IMAGE_BASE_URL=https://image.tmdb.org/t/p/w500

   DB_URL=jdbc:mysql://localhost:3306/db_filmes?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   DB_USERNAME=root
   DB_PASSWORD=sua_senha_do_mysql
   SERVER_PORT=8080
   ```
   > 💡 *Para obter sua chave gratuita da API do TMDB, acesse [themoviedb.org/settings/api](https://www.themoviedb.org/settings/api).*

3. **Inicie o servidor Spring Boot**:
   ```bash
   cd filmes
   ./mvnw spring-boot:run
   ```
   *(No Windows PowerShell, use `.\mvnw spring-boot:run`).*

O backend estará ativo em `http://localhost:8080`. O Hibernate criará as tabelas do banco de dados automaticamente.

---

### 2. Execução do Frontend

Você pode abrir o frontend de duas maneiras:

- **Via Live Server (VS Code)**: Abra a pasta `frontend/`, clique com o botão direito em `index.html` ou `login.html` e selecione **"Open with Live Server"**.
- **Diretamente no Navegador**: Dê um duplo clique no arquivo `frontend/index.html`.

---

### 📱 Servidor Portátil no Android (via Termux)

Este projeto foi projetado para rodar com baixíssimo consumo de energia em um smartphone Android transformado em servidor:

1. **Instale o Termux** (via [F-Droid](https://f-droid.org/packages/com.termux/)).
2. **Instale o OpenJDK e o MariaDB**:
   ```bash
   pkg update && pkg install openjdk-17 mariadb cloudflared -y
   ```
3. **Inicie o banco e crie a base**:
   ```bash
   mysqld_safe &
   mariadb -u root -e "CREATE DATABASE IF NOT EXISTS db_filmes; GRANT ALL PRIVILEGES ON *.* TO 'root'@'127.0.0.1'; FLUSH PRIVILEGES;"
   ```
4. **Execute o JAR do Spring Boot**:
   ```bash
   java -jar filmes-0.0.1-SNAPSHOT.jar --spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   ```
5. **Crie o túnel HTTPS público para conectar com o GitHub Pages**:
   ```bash
   cloudflared tunnel --url http://localhost:8080
   ```

---

## 📖 Documentação da API REST

### 👥 Usuários e Autenticação

| Método | Endpoint | Descrição | Status de Sucesso |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/usuarios` | Cadastra um novo usuário com preferências e gera recomendações | `201 Created` |
| `POST` | `/api/usuarios/login` | Autentica e-mail e senha via BCrypt | `200 OK` / `401 Unauthorized` |
| `GET` | `/api/usuarios` | Lista todos os usuários cadastrados | `200 OK` |
| `GET` | `/api/usuarios/{id}` | Retorna os detalhes de um usuário por ID | `200 OK` |
| `GET` | `/api/usuarios/{id}/recomendacoes` | Retorna a lista de filmes recomendados para o usuário | `200 OK` |

#### Exemplo de Payload de Login (`POST /api/usuarios/login`):
```json
{
  "email": "usuario@exemplo.com",
  "senha": "minhasenha123"
}
```

---

### 🍿 Catálogo e Pesquisa de Filmes

| Método | Endpoint | Parâmetros | Descrição |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/filmes/pesquisar` | `?termo=Matrix` | Pesquisa filmes por título/palavra-chave no TMDB |
| `GET` | `/api/filmes/populares` | *Nenhum* | Retorna os filmes em alta na semana |
| `GET` | `/api/filmes/filtro` | `?tipo=avaliados` | Filtra por categoria (`avaliados`, `scifi`, `terror`) |

---

## 🛡️ Segurança & Boas Práticas

- **Criptografia de Ponta a Ponta**: Todas as senhas cadastradas são protegidas com hash unidirecional **BCrypt (10 rounds de salt)**.
- **Proteção de Dados Sensíveis**: Propriedades como senhas de usuários e chaves de API estão isoladas do controle de versão via `.gitignore` e `JsonProperty(Access.WRITE_ONLY)`.
- **Prevenção contra Ataques**: Validação estrita de entradas com *Bean Validation* (`@NotBlank`, `@Email`, `@Min`, `@Max`, `@Size`).
- **Política CORS & Private Network Access (PNA)**: Filtros dedicados que garantem integração suave entre domínios públicos HTTPS (GitHub Pages) e instâncias de desenvolvimento locais.

---

## 📄 Licença

Este projeto está sob a licença **MIT**. Consulte o arquivo [LICENSE](LICENSE) para obter mais detalhes.

---

<div align="center">
  Desenvolvido por <b>Caike Santos</b> 🎬✨
</div>
