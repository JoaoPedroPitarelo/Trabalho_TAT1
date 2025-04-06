import "./Header.css"
import "@fontsource/roboto"
import "@fontsource/sansita-swashed"

function Header() {
    return( 
        // Não é HTML, parece, mas não é. É JSX
        <div className="header">
            <img src="images/logo.png" alt="Logo"/>
            <div className="content">
                <h1> ToDO App</h1>
                <p>Suas tarefas: </p>
            </div>
        </div>
    );
}

export default Header;