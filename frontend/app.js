/**
 * Script de Cadastro de Usuário e Perfil Cinematográfico
 */
document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('form');
    const submitBtn = document.querySelector('button[type="submit"]');
    const originalBtnText = submitBtn ? submitBtn.innerText : 'Cadastrar e Gerar Recomendações';

    // Cria contêiner para mensagens de feedback (Sucesso / Erro) se não existir
    let feedbackBox = document.getElementById('form-feedback');
    if (!feedbackBox) {
        feedbackBox = document.createElement('div');
        feedbackBox.id = 'form-feedback';
        feedbackBox.className = 'feedback-message';
        feedbackBox.style.display = 'none';
        form.parentNode.insertBefore(feedbackBox, form);
    }

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        // 1. Coleta os valores dos campos
        const nome = document.getElementById('nome')?.value?.trim();
        const email = document.getElementById('email')?.value?.trim();
        const senha = document.getElementById('senha')?.value;
        const nascimento = document.getElementById('nascimento')?.value || null;
        const idadeInput = document.getElementById('idade')?.value;
        const idade = idadeInput ? parseInt(idadeInput, 10) : null;

        // Formatos (múltiplas checkboxes)
        const formatos = Array.from(document.querySelectorAll('input[name="formato"]:checked'))
            .map(cb => cb.value);

        // Moods (múltiplas checkboxes)
        const moods = Array.from(document.querySelectorAll('input[name="mood"]:checked'))
            .map(cb => cb.value);

        // Frequência de maratona (radio)
        const frequenciaRadio = document.querySelector('input[name="frequencia"]:checked');
        const frequencia = frequenciaRadio ? frequenciaRadio.value : null;

        // Gênero favorito (select)
        const generoFavorito = document.getElementById('genero_favorito')?.value || null;

        // Subgêneros (múltiplas checkboxes)
        const subgeneros = Array.from(document.querySelectorAll('input[name="subgenero"]:checked'))
            .map(cb => cb.value);

        // Sliders de preferência (range)
        const pesoTecnologia = parseInt(document.getElementById('peso_tecnologia')?.value || '5', 10);
        const pesoEmocao = parseInt(document.getElementById('peso_emocao')?.value || '5', 10);
        const pesoComplexidade = parseInt(document.getElementById('peso_complexidade')?.value || '5', 10);

        // Tema de pesquisa rápida
        const temaPesquisa = document.getElementById('tema_pesquisa')?.value?.trim() || null;

        // Customização do perfil
        const corPerfil = document.getElementById('cor_perfil')?.value || '#e50914';
        const bio = document.getElementById('bio')?.value?.trim() || null;
        const origemCadastro = document.querySelector('input[name="origem_cadastro"]')?.value || 'formulario_web';

        // Avatar (nome do arquivo selecionado, se houver)
        const avatarInput = document.getElementById('avatar');
        const avatar = (avatarInput && avatarInput.files && avatarInput.files[0]) ? avatarInput.files[0].name : null;

        // 2. Monta o objeto payload do usuário
        const usuarioPayload = {
            nome,
            email,
            senha,
            nascimento,
            idade,
            formato: formatos,
            mood: moods,
            frequencia,
            generoFavorito,
            subgenero: subgeneros,
            pesoTecnologia,
            pesoEmocao,
            pesoComplexidade,
            temaPesquisa,
            corPerfil,
            avatar,
            bio,
            origemCadastro
        };

        // 3. Feedback visual de carregamento
        setLoadingState(true);
        esconderMensagem();

        // Determina a URL da API (suporta tanto rodando junto quanto Live Server ou porta 8080)
        const host = window.location.hostname === '127.0.0.1' ? '127.0.0.1' : 'localhost';
        const apiUrl = window.location.port === '8080' ? '/api/usuarios' : `http://${host}:8080/api/usuarios`;

        try {
            const response = await fetch(apiUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(usuarioPayload)
            });

            const data = await response.json();

            if (!response.ok) {
                // Trata mensagens de erro da API
                const errorMsg = data.message || data.mensagem || (data.errors ? Object.values(data.errors).join(', ') : 'Erro ao cadastrar usuário.');
                throw new Error(errorMsg);
            }

            // Salva os dados do usuário e as recomendações retornadas pelo backend
            if (data.usuario) {
                sessionStorage.setItem('cine_user', JSON.stringify(data.usuario));
            }
            if (data.recomendacoes) {
                sessionStorage.setItem('cine_recommendations', JSON.stringify(data.recomendacoes));
            }

            // Sucesso!
            const nomeUsuario = data.usuario?.nome || 'Usuário';
            exibirMensagem('success', `🎉 Parabéns, ${nomeUsuario}! Cadastro realizado com sucesso. Redirecionando para suas recomendações...`);
            form.reset();

            // Rola suavemente até o topo da mensagem de confirmação
            feedbackBox.scrollIntoView({ behavior: 'smooth' });

            // Redireciona para a tela inicial temporária após 1.2 segundos
            setTimeout(() => {
                window.location.href = 'home.html';
            }, 1200);

        } catch (error) {
            console.error('Erro na requisição de cadastro:', error);
            
            let userMessage = error.message;
            if (error.name === 'TypeError' && error.message.includes('Failed to fetch')) {
                userMessage = 'Não foi possível conectar ao servidor backend (http://localhost:8080). Verifique se o Spring Boot está em execução.';
            }

            exibirMensagem('error', `❌ ${userMessage}`);
            feedbackBox.scrollIntoView({ behavior: 'smooth' });
        } finally {
            setLoadingState(false);
        }
    });

    function setLoadingState(isLoading) {
        if (!submitBtn) return;
        submitBtn.disabled = isLoading;
        submitBtn.innerText = isLoading ? 'Cadastrando no sistema...' : originalBtnText;
        submitBtn.style.opacity = isLoading ? '0.7' : '1';
        submitBtn.style.cursor = isLoading ? 'not-allowed' : 'pointer';
    }

    function exibirMensagem(tipo, texto) {
        feedbackBox.innerText = texto;
        feedbackBox.className = `feedback-message feedback-${tipo}`;
        feedbackBox.style.display = 'block';
    }

    function esconderMensagem() {
        feedbackBox.style.display = 'none';
        feedbackBox.innerText = '';
    }
});
