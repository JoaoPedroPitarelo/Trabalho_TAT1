// src/pages/Home.js

import Header     from '../../components/Header/Header';
import Task       from '../../components/Task/Task'
import NewTask    from '../../components/NewTask/NewTask';
import UpdateTask from '../../components/UpdateTask/UpdateTask';
import { useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
// Importe a instância 'api' configurada com o interceptor de token
import api from '../../api/api'; 

// Use o caminho relativo aqui, a baseURL de api.js cuida do resto
const APIURL_TASKS = "/task"; // Renomeei para evitar conflito com API_URL do api.js

const Home = () => {
  const { currentUser, isAuthenticated, loading, logout } = useAuth();
  const [tasks, setTasks] = useState([]);
  const [tarefaParaEditar, setTarefaParaEditar] = useState(null);
  const [mostrarNewTask, setMostrarNewTask] = useState(false);
  const [mostrarUpdateTask, setMostrarUpdateTask] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');


  useEffect(() => {
    async function fetchTasks() {
      setErrorMessage(''); 
      try {
        const response = await api.get(APIURL_TASKS); // <-- Corrigido aqui
        setTasks(response.data.tasks);
      } catch (error) {
        console.error("Erro ao buscar tarefas:", error);
        if (error.response && error.response.status === 401) {
          setErrorMessage("Sessão expirada ou não autorizado. Por favor, faça login novamente.");
        } else {
          setErrorMessage("Erro ao buscar tarefas. Por favor, tente novamente.");
        }
      }
    }

    if (!loading && isAuthenticated()) {
      fetchTasks();
    }
  }, [isAuthenticated, loading, logout]); 

  const completeTask = async (id) => {
    setErrorMessage('');
    try {
        let response = await api.delete(`${APIURL_TASKS}/${id}`); 
        setTasks(prev => prev.filter(task => task.id !== id)); 
        console.log("Tarefa deletada:", response.data);
    } catch (error) {
        console.error("Erro ao deletar uma tarefa: ", error);
        if (error.response && error.response.status === 401) {
          // setErrorMessage("Não autorizado para deletar. Por favor, faça login novamente.");
        } else {
          setErrorMessage("Erro ao deletar tarefa. Por favor, tente novamente.");
        }
    }  
  };

  // Salva a tarefa no banco de dados
  const salveTask = async (novaTarefa) => {    
    setErrorMessage('');
    try {
      let response = await api.post(`${APIURL_TASKS}`, {"title": novaTarefa.title, "description": novaTarefa.description});
      
      const tarefaCriada = response.data;
      
      setTasks((prev) => [...prev, tarefaCriada]);
      setMostrarNewTask(false); 
      console.log("Tarefa salva:", response.data);
    } catch (error) {
      console.error("Erro ao salvar tarefa: ", error);
      if (error.response && error.response.status === 401) {
        // setErrorMessage("Não autorizado para salvar. Por favor, faça login novamente.");
      } else {
        setErrorMessage("Erro ao salvar tarefa. Por favor, tente novamente.");
      }
    }
  }

  const prepararEdicao = (tarefa) => {
    setTarefaParaEditar(tarefa);
    setMostrarUpdateTask(true);
  };
  
  const updateTask = async (id, title, description) => {
    setErrorMessage('');
    
    try {
      let response = await api.put(`${APIURL_TASKS}/${id}`, {"title": title, "description": description, "completed": false}); // <-- Usando APIURL_TASKS
      setTasks(prev => prev.map(task => 
        task.id === id ? response.data : task 
      ));
      console.log("Tarefa atualizada:", response.data);
      setMostrarUpdateTask(false); 
    } catch (error) {
      console.error("Erro ao modificar a tarefa: ", error);
      if (error.response && error.response.status === 401) {
        // setErrorMessage("Não autorizado para atualizar. Por favor, faça login novamente.");
      } else {
        setErrorMessage("Erro ao modificar tarefa. Por favor, tente novamente.");
      }
    }
  }
  
  if (loading) {
    return <div>Carregando informações do usuário...</div>;
  }

  return (
    <div className="App">
      <Header /> 

      {errorMessage && <p style={{ color: 'red', textAlign: 'center' }}>{errorMessage}</p>}

      {isAuthenticated() ? (
        <>
          {currentUser && <h3 style={{ textAlign: 'center', margin: '20px 0', color: "white" }}>Bem-vindo, {currentUser.username}!</h3>}

          { mostrarNewTask && ( <NewTask salveTask={salveTask} onClose={() => setMostrarNewTask(false)} />) }
          { mostrarUpdateTask && ( 
            <UpdateTask 
              tarefa={tarefaParaEditar} 
              updateTask={updateTask}  
              onClose={() => setMostrarUpdateTask(false)}/>) }
          
          <button className='novaTask' onClick={() => setMostrarNewTask(true)} style={{ display: 'block', margin: '20px auto', padding: '10px 20px', fontSize: '16px', cursor: 'pointer' }}>Nova tarefa +</button>

          {tasks.length > 0 ? (
            tasks.map((task, index) => (
              <Task 
                key={index}
                id={task.id}
                title={task.title}
                description={task.description}
                completeTask={completeTask}
                mostrarDetalhes={() => prepararEdicao(task)}
              />
            ))
          ) : (
            <p style={{ textAlign: 'center' }}>Nenhuma tarefa encontrada. Crie uma nova!</p>
          )}
        </>
      ) : (
        <div style={{ textAlign: 'center', marginTop: '50px' }}>
          <h2>Você precisa estar logado para visualizar suas tarefas.</h2>
          <p>Por favor, <a href="/login">faça login</a> ou <a href="/register">registre-se</a>.</p>
        </div>
      )}
    </div>
  );
}

export default Home;
