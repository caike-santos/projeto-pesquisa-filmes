/**
 * Script da Tela Inicial (Dashboard de Filmes Recomendados)
 */
document.addEventListener('DOMContentLoaded', () => {
    // 1. Recupera dados do usuário salvos no cadastro (ou perfil padrão de demonstração)
    const storedUser = sessionStorage.getItem('cine_user');
    const storedRecs = sessionStorage.getItem('cine_recommendations');

    let usuario = null;
    let filmes = [];

    if (storedUser) {
        try {
            usuario = JSON.parse(storedUser);
        } catch (e) {
            console.error('Erro ao ler dados do usuário:', e);
        }
    }

    if (storedRecs) {
        try {
            filmes = JSON.parse(storedRecs);
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
    if (!filmes || filmes.length === 0) {
        filmes = obterFilmesPadrao();
    }

    // 2. Atualiza os elementos da interface com o perfil do usuário
    renderizarPerfilUsuario(usuario);

    // 3. Renderiza o grid de filmes
    renderizarFilmes(filmes);

    // 4. Configura a busca / filtro instantâneo
    const searchInput = document.getElementById('search-movies');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const termo = e.target.value.toLowerCase().trim();
            if (!termo) {
                renderizarFilmes(filmes);
                return;
            }

            const filtrados = filmes.filter(f => {
                const titulo = (f.titulo || '').toLowerCase();
                const sinopse = (f.sinopse || '').toLowerCase();
                const generos = (f.generos || []).join(' ').toLowerCase();
                const motivo = (f.motivoRecomendacao || '').toLowerCase();
                return titulo.includes(termo) || sinopse.includes(termo) || generos.includes(termo) || motivo.includes(termo);
            });

            renderizarFilmes(filtrados);
        });
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

    // Aplica a cor customizada escolhida no cadastro
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

    // Tags de preferências
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

    if (listaFilmes.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <h3>Nenhum filme encontrado</h3>
                <p>Tente buscar por outro termo ou gênero cinematográfico.</p>
            </div>
        `;
        return;
    }

    listaFilmes.forEach(filme => {
        const card = document.createElement('article');
        card.className = 'movie-card';

        const poster = filme.posterUrl || 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=500&auto=format&fit=crop&q=60';
        const ano = filme.dataLancamento ? filme.dataLancamento.split('-')[0] : '2024';
        const nota = filme.notaMedia ? filme.notaMedia.toFixed(1) : '8.5';
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

/**
 * Catálogo de fallback se acessado diretamente
 */
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
