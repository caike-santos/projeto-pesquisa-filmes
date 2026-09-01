/**
 * Script de Login e Autenticação de Usuário
 */
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('login-form');
    const submitBtn = document.getElementById('btn-login');
    const feedbackBox = document.getElementById('login-feedback');
    const originalBtnText = submitBtn ? submitBtn.innerText : 'Entrar no CinePerfil';

    function getApiUrl(endpoint) {
        if (window.API_CONFIG) {
            return window.API_CONFIG.getUrl(endpoint);
        }
        return endpoint;
    }

    if (form) {
        form.addEventListener('submit', async (event) => {
            event.preventDefault();

            const email = document.getElementById('email')?.value?.trim();
            const senha = document.getElementById('senha')?.value;

            if (!email || !senha) {
                exibirMensagem('error', 'Por favor, preencha todos os campos.');
                return;
            }

            setLoadingState(true);
            esconderMensagem();

            try {
                const url = getApiUrl('/api/usuarios/login');
                const response = await fetch(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    },
                    body: JSON.stringify({ email, senha })
                });

                const data = await response.json();

                if (!response.ok) {
                    const errorMsg = data.mensagem || data.message || 'Credenciais inválidas. Verifique seu e-mail e senha.';
                    throw new Error(errorMsg);
                }

                // Salva os dados do usuário e as recomendações na sessão
                if (data.usuario) {
                    sessionStorage.setItem('cine_user', JSON.stringify(data.usuario));
                }
                if (data.recomendacoes) {
                    sessionStorage.setItem('cine_recommendations', JSON.stringify(data.recomendacoes));
                }

                const nomeUsuario = data.usuario?.nome || 'Usuário';
                exibirMensagem('success', `🎉 Bem-vindo(a) de volta, ${nomeUsuario}! Entrando...`);

                // Redireciona para a tela inicial
                setTimeout(() => {
                    window.location.href = 'home.html';
                }, 1000);

            } catch (error) {
                console.error('Erro no login:', error);

                let userMessage = error.message;
                if (error.name === 'TypeError' && error.message.includes('Failed to fetch')) {
                    userMessage = 'Não foi possível conectar ao servidor backend. Verifique se o Spring Boot está em execução.';
                }

                exibirMensagem('error', `❌ ${userMessage}`);
            } finally {
                setLoadingState(false);
            }
        });
    }

    function setLoadingState(isLoading) {
        if (!submitBtn) return;
        submitBtn.disabled = isLoading;
        submitBtn.innerText = isLoading ? 'Verificando credenciais...' : originalBtnText;
        submitBtn.style.opacity = isLoading ? '0.7' : '1';
        submitBtn.style.cursor = isLoading ? 'not-allowed' : 'pointer';
    }

    function exibirMensagem(tipo, texto) {
        if (!feedbackBox) return;
        feedbackBox.innerText = texto;
        feedbackBox.className = `feedback-message feedback-${tipo}`;
        feedbackBox.style.display = 'block';
    }

    function esconderMensagem() {
        if (!feedbackBox) return;
        feedbackBox.style.display = 'none';
        feedbackBox.innerText = '';
    }
});
