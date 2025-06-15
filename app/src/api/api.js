
import axios from "axios";
// Importe seu serviço de autenticação para ter acesso ao token e logout
// Certifique-se que o caminho está correto para o seu arquivo auth.js (ou AuthService.js)
import AuthService from './auth'; 

const API_URL = "http://localhost:8080";

const api = axios.create({
    baseURL: API_URL,
    headers: {
        "Content-Type": "application/json"
    }
});

// --- Interceptor de Requisição: Adiciona o Token JWT ---
// Este interceptor será executado ANTES de cada requisição ser enviada.
api.interceptors.request.use(
    (config) => {
        // Pega o token JWT do AuthService
        const token = AuthService.getToken(); 

        // Se um token existir, ele é adicionado ao cabeçalho Authorization
        if (token) {
            // O formato padrão para JWT é "Bearer [token]"
            config.headers["Authorization"] = "Bearer " + token;
        }
        // Retorna a configuração da requisição modificada
        return config; 
    },
    (error) => {
        // Lida com erros que ocorrem antes da requisição ser enviada (raro)
        return Promise.reject(error);
    }
);

// --- Interceptor de Resposta (Opcional, mas Recomendado para 401s globais) ---
// Este interceptor será executado APÓS cada resposta ser recebida.
api.interceptors.response.use(
    (response) => response, // Se a resposta for bem-sucedida, apenas a repassa
    async (error) => {
        // Verifica se é um erro 401 (Não Autorizado) e se não é a própria requisição de login
        // Isso evita um loop infinito se a API de login retornar 401
        if (error.response && error.response.status === 401 && error.config && 
            !error.config.url.includes("/user/login") && !error.config.url.includes("/user/create")) {
            
            console.warn("401 Unauthorized: Sessão expirada ou token inválido. Deslogando...");
            // Chama a função logout do AuthService para limpar o token e redirecionar
            AuthService.logout(); 
            // Rejeita a promise para que o erro ainda possa ser tratado no componente que fez a chamada
            return Promise.reject(error);
        }
        // Para outros tipos de erros, apenas rejeita a promise
        return Promise.reject(error); 
    }
);

export default api; // Exporta a instância 'api' configurada
