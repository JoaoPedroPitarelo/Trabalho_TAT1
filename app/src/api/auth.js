import api from "./api.js";

const TOKEN_KEY = "TOKEN_JWT";

const AuthService = {
    async login(username, password) {
        try {
            const response = await api.post("/user/login", { username, password });
            if (response.data.accessJWT) {
                localStorage.setItem("TOKEN_JWT", JSON.stringify(response.data));
            }
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    async register(username, email, password) {
        try {
            const response = await api.post("/user/create", { username, email, password})
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    logout() {
        localStorage.removeItem(TOKEN_KEY);
    },

    getCurrentUser() {
        const user = localStorage.getItem(TOKEN_KEY);
        return user ? JSON.parse(user) : null;
    },

    getToken() {
        const user = this.getCurrentUser();
        return user ? user.accessJWT : null
    }
}

export default AuthService;
