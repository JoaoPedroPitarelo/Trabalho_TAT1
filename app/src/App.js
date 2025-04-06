import Header     from './components/Header/Header';
import Task       from './components/Task/Task'
import NewTask    from './components/NewTask/NewTask';
import UpdateTask from './components/UpdateTask/UpdateTask';
import axios from 'axios';
import { useEffect, useState } from 'react';

const APIURL = "http://localhost:8080/task";


function App() {
  const [tasks, setTasks] = useState([]);
  const [tarefaParaEditar, setTarefaParaEditar] = useState(null);
  const [mostrarNewTask, setMostrarNewTask] = useState(false);
  const [mostrarUpdateTask, setMostrarUpdateTask] = useState(false);

  // Faz a requisição para API e set a lista de task 'setTask(response.data)'
  // Na real estamos declarando o arrowFunc dentro do useEffect e depois chamando esse arrowFunc
  // O useEffect é um método que é chamado logo após os componentes serem renderizados na página, mount
  useEffect(() => {
    async function fetchTasks() {
      try {
        const response = await axios.get(APIURL);
        setTasks(response.data);
      } catch (error) {
        console.error("Erro ao buscar tarefas:", error);
      }
    }

    fetchTasks();
  }, []);

  // Marcar a tarefa como concluída
  const completeTask = async (id) => {
    try {
        let response = await axios.delete(`${APIURL}/${id}`); // manda a requisição para a API
        setTasks(prev => prev.filter(task => task.id !== id)); // retira da lista de tarefas e atualiza o estado
        console.log(response.data);
    } catch (erro) {
        console.error("Erro ao deletar uma tarefa: ", erro);
    }  
  };

  // Salva a tarefa no banco de dados
  const salveTask = async (novaTarefa) => {    
    try {
      let response = await axios.post(`${APIURL}`, {"title": novaTarefa.title, "description": novaTarefa.description})
      
      const tarefaCriada = response.data
      
      setTasks((prev) => [...prev, tarefaCriada]);
      setMostrarNewTask(false)
      console.log(response)
    } catch (erro) {
      console.error("Deu ruim paizao: ", erro)
    }
  }

  // Update Task
  const prepararEdicao = (tarefa) => {
    setTarefaParaEditar(tarefa);
    setMostrarUpdateTask(true);
  };
  
  const updateTask = async (id, title, description) => {
    setMostrarUpdateTask(true); 
    
    try {
      let response = await axios.put(`${APIURL}/${id}`, {"title": title, "description": description, "completed": false})
      setTasks(prev => prev.map(task => 
        task.id === id ? response.data : task // substitui pela tarefa retornada da requisição, senão mantêm
      ))
      console.log(response)
    } catch (error) {
      console.error("Erro ao modificar a tarefa: ", error)
    }
  }
  
  return (
    <div className="App">
      <Header /> 

      { mostrarNewTask && ( <NewTask salveTask={salveTask} onClose={() => setMostrarNewTask(false)} />) }
      { mostrarUpdateTask && ( 
        <UpdateTask 
          tarefa={tarefaParaEditar} 
          updateTask={updateTask}  
          onClose={() => setMostrarUpdateTask(false)}/>) }
      <button className='novaTask' onClick={() => setMostrarNewTask(true)}>Nova tarefa +</button>

      {tasks.map((task, index) => (
        <Task 
          key={index}
          id={task.id}
          title={task.title}
          description={task.description}
          completeTask={completeTask}
          mostrarDetalhes={() => prepararEdicao(task)}
        />
      ))}
    </div>
  );

}

export default App;
