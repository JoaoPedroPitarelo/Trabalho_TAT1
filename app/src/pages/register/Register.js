import React, { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { Link, useNavigate } from "react-router-dom"; // Importe useNavigate
import "./register.css";

const Register = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const { register } = useAuth();
    const navigate = useNavigate(); // Inicialize useNavigate

    const handleRegister = async (e) => {
        e.preventDefault();
        setMessage(""); // Limpa mensagens anteriores
        try {
            const response = await register(username, email, password);           
            setMessage("Cadastro realizado com sucesso! Redirecionando para o login...");
            setTimeout(() => {
                navigate("/login"); 
            }, 2000); // Redireciona após 2 segundos
      
        } catch (error) {
            // Tratamento de erro como você já estava fazendo
            const resMessage =
                (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString();
            setMessage(resMessage);
        }
    }

    return (
        // Contêiner externo para centralização na página
        <div className="login-container"> {/* Reutilizando a classe do login para centralização */}
            {/* Contêiner interno para o card de registro */}
            <div className="content"> 
                <h2>Cadastro ToDo</h2>
                <form onSubmit={handleRegister}>
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
                        <label>Email:</label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label>Password:</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    
                    <button type="submit">Cadastrar</button> {/* Mudei o texto do botão para "Cadastrar" */}
                    {message && <p className={message.includes("sucesso") ? "success-message" : "error-message"}>{message}</p>}
                </form>
                <p>
                    Já tem uma conta? <Link to="/login">Entre aqui!</Link>
                </p>
            </div>
        </div>
    );
}

export default Register;
