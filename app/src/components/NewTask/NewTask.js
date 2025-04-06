import { useState } from "react";
import "./NewTask.css"

function NewTask({ onClose, salveTask }) {
    const [titulo, setTitulo] = useState("");
    const [descricao, setDescricao] = useState("");

    const handleSalvar = () => {
        // aqui você pode montar um objeto e passar para o salveTask
        const novaTarefa = {
            title: titulo,
            description: descricao
        };
        salveTask(novaTarefa);
    };
    
    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>Adicionar nova Tarefa</h2>
                
                <div className="form">
                    <input 
                        type="text" 
                        placeholder="Título"
                        value={titulo}
                        onChange={(e) => setTitulo(e.target.value)}
                    />
                    <textarea 
                        placeholder="Descrição"
                        value={descricao}
                        onChange={(e) => setDescricao(e.target.value)}
                    ></textarea>
                </div>

                <div className="buttons">
                    <button onClick={onClose} id="cancelar">Cancelar</button>
                    <button id="salvar" onClick={handleSalvar}>Salvar</button> 
                </div>

            </div>
        </div>
    );
}

export default NewTask;