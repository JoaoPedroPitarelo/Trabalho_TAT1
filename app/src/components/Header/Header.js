import "./Header.css"
import "@fontsource/roboto"
import "@fontsource/sansita-swashed"
import { useAuth } from "../../context/AuthContext";


function Header() {
    const { logout } = useAuth();

    return (
        // Não é HTML, parece, mas não é. É JSX
        <div className="header">
            
            <div className="navbar">
                <img src="images/logo.png" alt="Logo" />            
                
                <button className="logout-button" onClick={logout}>Logout</button>
            </div>
            <div className="content">
                <h1> ToDO App</h1>
                <p>Suas tarefas: </p>
            </div>
        </div>
    );
}

export default Header;