import React, { useState } from "react";
import "@fontsource/roboto"
import "@fontsource/sansita-swashed"
import { useAuth } from "../../context/AuthContext";
import { Link } from "react-router-dom";
import "./login.css"

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const { login } = useAuth();

    const handleLogin = async (e) => {
        e.preventDefault();
        setMessage("");
        try {
            await login(username, password);
        } catch (error) {
            const resMessage =
                (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString();
            setMessage(resMessage);
        }
    };

    return (
        <div className="login-container">
            <div className="content">
                <h2>Login ToDO</h2>
                <form onSubmit={handleLogin}>
                    <div>
                        <label>Username:</label>
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label>Senha:</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit">Login</button>
                    {message && <p>{message}</p>}
                </form>
                <p>
                    Não tem uma conta? <Link to="/register">Faça aqui</Link>
                </p>
            </div>
        </div>
    );
};

export default Login;