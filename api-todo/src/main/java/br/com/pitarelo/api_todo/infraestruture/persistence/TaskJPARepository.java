package br.com.pitarelo.api_todo.infraestruture.persistence;

import br.com.pitarelo.api_todo.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskJPARepository extends JpaRepository<Task, Long> {
    List<Task> findByCompletedFalse();
}
