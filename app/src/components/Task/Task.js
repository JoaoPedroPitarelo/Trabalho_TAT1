import "./Task.css"

function Task({id, title, description, completeTask, mostrarDetalhes}) {        
  return (
      <div className="tarefa-card">
        <button className="faixa-lateral" onClick={() => completeTask(id)}></button>
        <button className="botao-detalhes" onClick={() => mostrarDetalhes(true)}>
          <div className="conteudo">
            <h2 className="titulo">{title}</h2>
            <p className="descricao">
            {description}
            </p>
          </div>
        </button>
      </div>
    );
  }
  
export default Task