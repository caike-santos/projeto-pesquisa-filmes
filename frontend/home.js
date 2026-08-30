/**
 * Script da Tela Inicial (Dashboard de Filmes e Pesquisa Global)
 */
document.addEventListener('DOMContentLoaded', () => {
    // 1. Recupera dados do usuário salvos no cadastro (ou perfil padrão de demonstração)
    const storedUser = sessionStorage.getItem('cine_user');
    const storedRecs = sessionStorage.getItem('cine_recommendations');

    let usuario = null;
    let filmesIniciais = [];
    let filmesAtuais = [];

    if (storedUser) {
        try {
            usuario = JSON.parse(storedUser);
        } catch (e) {
            console.error('Erro ao ler dados do usuário:', e);
        }
    }

    if (storedRecs) {
        try {
            filmesIniciais = JSON.parse(storedRecs);
        } catch (e) {
            console.error('Erro ao ler recomendações:', e);
        }
    }

    // Se não tiver usuário no sessionStorage (ex: acesso direto), usa um perfil padrão
    if (!usuario) {
        usuario = {
            nome: 'Explorador(a) de Cinema',
            generoFavorito: 'sci_fi',
            temaPesquisa: 'Cyberpunk e Distopia',
            mood: ['suspense', 'reflexivo'],
            formato: ['filmes', 'series'],
            corPerfil: '#e50914',
            bio: 'Descobrindo novas histórias e universos cinematográficos.'
        };
    }

    // Se não tiver filmes no sessionStorage, usa catálogo padrão
    if (!filmesIniciais || filmesIniciais.length === 0) {
        filmesIniciais = obterFilmesPadrao();
    }

    filmesAtuais = [...filmesIniciais];

    // 2. Renderiza dados do usuário na interface
    renderizarPerfilUsuario(usuario);

    // 3. Renderiza catálogo inicial
    renderizarFilmes(filmesAtuais);

    // Determina a URL base da API (suporta Live Server 5500 ou mesma porta 8080)
    const apiBaseUrl = window.location.port === '8080' ? '/api/filmes' : 'http://localhost:8080/api/filmes';

    // 4. Configura o Formulário de Pesquisa Global de Filmes
    const searchForm = document.getElementById('global-search-form');
    const searchInput = document.getElementById('global-search-input');
    const sectionTitle = document.getElementById('section-title-text');

    if (searchForm && searchInput) {
        searchForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const termo = searchInput.value.trim();
            if (!termo) return;

            await executarBuscaGlobal(termo);
        });
    }

    // Botão de Logout
    const logoutBtn = document.getElementById('btn-logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            sessionStorage.removeItem('cine_user');
            sessionStorage.removeItem('cine_recommendations');
            window.location.href = 'login.html';
        });
    }

    // 5. Configura os Chips de Filtros Rápidos (Em Alta, Avaliados, etc.)
    const quickTags = document.querySelectorAll('.quick-filter-tag');
    quickTags.forEach(tag => {
        tag.addEventListener('click', async () => {
            const filtro = tag.getAttribute('data-filter');
            if (filtro === 'reset') {
                filmesAtuais = [...filmesIniciais];
                if (sectionTitle) sectionTitle.innerHTML = '<span>🍿</span> Filmes e Séries Recomendados para Você';
                if (searchInput) searchInput.value = '';
                renderizarFilmes(filmesAtuais);
                return;
            }

            await executarBuscaFiltro(filtro, tag.innerText);
        });
    });

    // 6. Configura o filtro instantâneo local da lista exibida
    const localFilterInput = document.getElementById('search-movies');
    if (localFilterInput) {
        localFilterInput.addEventListener('input', (e) => {
            const termo = e.target.value.toLowerCase().trim();
            if (!termo) {
                renderizarFilmes(filmesAtuais);
                return;
            }

            const filtrados = filmesAtuais.filter(f => {
                const titulo = (f.titulo || '').toLowerCase();
                const sinopse = (f.sinopse || '').toLowerCase();
                const generos = (f.generos || []).join(' ').toLowerCase();
                const motivo = (f.motivoRecomendacao || '').toLowerCase();
                return titulo.includes(termo) || sinopse.includes(termo) || generos.includes(termo) || motivo.includes(termo);
            });

            renderizarFilmes(filtrados);
        });
    }

    /**
     * Função que realiza a busca no Web Service do backend
     */
    async function executarBuscaGlobal(termo) {
        exibirCarregando(`Buscando por "${termo}" no Web Service...`);

        try {
            const response = await fetch(`${apiBaseUrl}/pesquisar?termo=${encodeURIComponent(termo)}`);
            if (!response.ok) throw new Error('Falha ao comunicar com o servidor');

            const resultados = await response.json();
            filmesAtuais = resultados;

            if (sectionTitle) {
                sectionTitle.innerHTML = `<span>🔍</span> Resultados para: "<em>${termo}</em>" <small style="font-size: var(--font-size-xs); color: var(--text-secondary);">(${resultados.length} encontrados)</small>`;
            }

            renderizarFilmes(filmesAtuais);

            // Rola suavemente até os resultados
            const grid = document.getElementById('movies-grid');
            if (grid) grid.scrollIntoView({ behavior: 'smooth', block: 'start' });

        } catch (error) {
            console.error('Erro na pesquisa de filmes:', error);
            // Fallback de pesquisa local se backend estiver offline
            const filtrados = filmesIniciais.filter(f => 
                (f.titulo || '').toLowerCase().includes(termo.toLowerCase()) ||
                (f.sinopse || '').toLowerCase().includes(termo.toLowerCase())
            );
            filmesAtuais = filtrados.length > 0 ? filtrados : filmesIniciais;
            renderizarFilmes(filmesAtuais);
        }
    }

    /**
     * Função que realiza busca por categorias rápidas
     */
    async function executarBuscaFiltro(filtro, label) {
        exibirCarregando(`Carregando filmes (${label})...`);

        try {
            const response = await fetch(`${apiBaseUrl}/filtro?tipo=${encodeURIComponent(filtro)}`);
            if (!response.ok) throw new Error('Falha ao consultar categoria');

            const resultados = await response.json();
            filmesAtuais = resultados;

            if (sectionTitle) {
                sectionTitle.innerHTML = `<span>${label.split(' ')[0]}</span> Categoria: ${label} <small style="font-size: var(--font-size-xs); color: var(--text-secondary);">(${resultados.length} filmes)</small>`;
            }

            renderizarFilmes(filmesAtuais);

            const grid = document.getElementById('movies-grid');
            if (grid) grid.scrollIntoView({ behavior: 'smooth', block: 'start' });

        } catch (error) {
            console.error('Erro ao buscar filtro:', error);
            renderizarFilmes(filmesAtuais);
        }
    }

    function exibirCarregando(mensagem) {
        const container = document.getElementById('movies-grid');
        if (container) {
            container.innerHTML = `
                <div class="empty-state" style="border-style: solid;">
                    <div style="font-size: 2rem; margin-bottom: 0.5rem;">⏳</div>
                    <h3>${mensagem}</h3>
                    <p style="color: var(--text-muted);">Consultando API de filmes...</p>
                </div>
            `;
        }
    }
});

/**
 * Renderiza o cabeçalho e os metadados do usuário cadastrado
 */
function renderizarPerfilUsuario(usuario) {
    const displayName = document.getElementById('user-display-name');
    const avatar = document.getElementById('user-avatar');
    const greeting = document.getElementById('hero-greeting');
    const bio = document.getElementById('hero-bio');
    const tagsContainer = document.getElementById('hero-tags');
    const heroSection = document.getElementById('welcome-hero');

    if (displayName) displayName.innerText = usuario.nome;
    if (greeting) greeting.innerText = `Olá, ${usuario.nome}! 🎬`;

    if (usuario.corPerfil) {
        if (heroSection) heroSection.style.borderLeftColor = usuario.corPerfil;
        if (avatar) avatar.style.backgroundColor = usuario.corPerfil;
    }

    if (avatar && usuario.nome) {
        avatar.innerText = usuario.nome.charAt(0).toUpperCase();
    }

    if (bio) {
        bio.innerText = usuario.bio || 'Aqui estão as melhores recomendações selecionadas com base nas suas respostas no cadastro.';
    }

    if (tagsContainer) {
        tagsContainer.innerHTML = '';

        if (usuario.generoFavorito) {
            adicionarTag(tagsContainer, `⭐ Gênero: ${usuario.generoFavorito}`);
        }
        if (usuario.temaPesquisa) {
            adicionarTag(tagsContainer, `🔍 Tema: ${usuario.temaPesquisa}`);
        }
        if (usuario.mood && Array.isArray(usuario.mood)) {
            usuario.mood.forEach(m => adicionarTag(tagsContainer, `✨ Clima: ${m}`));
        }
        if (usuario.formato && Array.isArray(usuario.formato)) {
            usuario.formato.forEach(fmt => adicionarTag(tagsContainer, `📺 Formato: ${fmt}`));
        }
    }
}

function adicionarTag(container, texto) {
    const span = document.createElement('span');
    span.className = 'hero-tag';
    span.innerText = texto;
    container.appendChild(span);
}

/**
 * Renderiza os cards de filmes no container
 */
function renderizarFilmes(listaFilmes) {
    const container = document.getElementById('movies-grid');
    if (!container) return;

    container.innerHTML = '';

    if (!listaFilmes || listaFilmes.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <div style="font-size: 2.5rem; margin-bottom: 0.5rem;">🎬</div>
                <h3>Nenhum filme encontrado</h3>
                <p>Tente buscar por outro título, palavra-chave ou utilize os filtros rápidos acima.</p>
            </div>
        `;
        return;
    }

    listaFilmes.forEach(filme => {
        const card = document.createElement('article');
        card.className = 'movie-card';

        const poster = filme.posterUrl || 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=500&auto=format&fit=crop&q=60';
        const ano = filme.dataLancamento ? filme.dataLancamento.split('-')[0] : '2024';
        const nota = filme.notaMedia ? filme.notaMedia.toFixed(1) : '8.0';
        const generos = filme.generos && filme.generos.length > 0 ? filme.generos : ['Cinema'];
        const motivo = filme.motivoRecomendacao || 'Selecionado para combinar com suas preferências.';

        card.innerHTML = `
            <div class="movie-poster-wrapper">
                <img src="${poster}" alt="${filme.titulo}" class="movie-poster" loading="lazy" onerror="this.src='https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=500&auto=format&fit=crop&q=60'">
                <div class="movie-rating-badge">★ ${nota}</div>
            </div>
            <div class="movie-info">
                <h4 class="movie-title">${filme.titulo}</h4>
                <span class="movie-year">${ano}</span>
                <div class="movie-genres">
                    ${generos.map(g => `<span class="genre-tag">${g}</span>`).join('')}
                </div>
                <p class="movie-overview">${filme.sinopse || 'Uma experiência audiovisual imperdível recomendada para seu perfil.'}</p>
                <div class="movie-reason">
                    💡 ${motivo}
                </div>
            </div>
        `;

        container.appendChild(card);
    });
}

function obterFilmesPadrao() {
    return [
        {
            id: 1,
            titulo: "Blade Runner 2049",
            sinopse: "Um novo policial de Los Angeles desenterra um segredo enterrado há muito tempo que tem o potencial de mergulhar o que resta da sociedade no caos.",
            dataLancamento: "2017-10-06",
            notaMedia: 8.0,
            generos: ["Ficção Científica", "Cyberpunk", "Mistério"],
            posterUrl: "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=500&auto=format&fit=crop&q=60",
            motivoRecomendacao: "Combina perfeitamente com seu alto interesse em Cyberpunk e Tecnologia."
        },
        {
            id: 2,
            titulo: "Interestelar",
            sinopse: "Uma equipe de exploradores viaja através de um buraco de minhoca no espaço, na tentativa de garantir a sobrevivência da humanidade.",
            dataLancamento: "2014-11-07",
            notaMedia: 8.7,
            generos: ["Ficção Científica", "Drama", "Aventura"],
            posterUrl: "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=500&auto=format&fit=crop&q=60",
            motivoRecomendacao: "Ideal para quem busca exploração espacial e forte carga emocional."
        },
        {
            id: 3,
            titulo: "A Origem",
            sinopse: "Um ladrão que rouba segredos corporativos através do uso da tecnologia de compartilhamento de sonhos recebe a tarefa inversa de plantar uma ideia.",
            dataLancamento: "2010-07-16",
            notaMedia: 8.8,
            generos: ["Ação", "Suspense", "Ficção Científica"],
            posterUrl: "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=500&auto=format&fit=crop&q=60",
            motivoRecomendacao: "Trama de altíssima complexidade e suspense psicológico constante."
        },
        {
            id: 4,
            titulo: "Duna: Parte 2",
            sinopse: "Paul Atreides se une a Chani e aos Fremen em busca de vingança contra os conspiradores que destruíram sua família.",
            dataLancamento: "2024-03-01",
            notaMedia: 8.6,
            generos: ["Ficção Científica", "Aventura", "Fantasia"],
            posterUrl: "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=500&auto=format&fit=crop&q=60",
            motivoRecomendacao: "Para quem gosta de cenários épicos e narrativas grandiosas."
        }
    ];
}
