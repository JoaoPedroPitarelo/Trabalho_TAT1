// src/context/AuthContext.js

import React, { createContext, useState, useEffect, useContext } from "react";
import AuthService from "../api/auth";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    // CORREÇÃO AQUI: Renomear 'getCurrentUser' para 'currentUser' para consistência
    const [currentUser, setCurrentUser] = useState(null); 
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const user = AuthService.getCurrentUser();
        if (user) {
            setCurrentUser(user);
        }
        setLoading(false);
    }, []);

    const login = async (username, password) => {
        try {
            const user = await AuthService.login(username, password);
            setCurrentUser(user);
            navigate("/");
            return user;
        } catch (error) {
            console.log("Login failed: ", error);
            throw error;
        }
    };

    const register = async (username, email, password) => {
        try {
            const response = await AuthService.register(username, email, password);
            return response;
        } catch (error) {
            console.log("Register error: ", error);
            throw error;
        }
    };

    const logout = () => {
        AuthService.logout();
        setCurrentUser(null);
        navigate('/login');
    };

    const isAuthenticated = () => {
        return !!currentUser && !!AuthService.getToken(); // Usa 'currentUser' corrigido
    }

    return (
        // Fornece 'currentUser' no contexto
        <AuthContext.Provider value={{ currentUser, isAuthenticated, login, register, logout, loading }}>
            {!loading && children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => {
    return useContext(AuthContext);
}
