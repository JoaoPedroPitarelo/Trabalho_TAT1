import './UpdateTask.css';
import { useState } from "react";


function UpdateTask({ onClose, tarefa, updateTask }) {
    const [titulo, setTitulo] = useState(tarefa.title);
    const [descricao, setDescricao] = useState(tarefa.description);

    const handleSalvar = () => {
        updateTask(tarefa.id, titulo, descricao);
        onClose();
    };
    
    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>Modificar</h2>
                
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
                    <button id="salvar" onClick={handleSalvar}>Salvar Mudanças</button> 
                </div>

            </div>
        </div>
    );
}

export default UpdateTask;