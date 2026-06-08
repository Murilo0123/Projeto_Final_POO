package com.faculdade.taskmanager.repository;

import com.faculdade.taskmanager.model.Task;
import com.faculdade.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório JPA para a entidade Task.
 * Extende JpaRepository fornecendo operações CRUD automaticamente.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Busca tarefas pelo status.
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Busca tarefas pelo ID da categoria.
     */
    List<Task> findByCategoryId(Long categoryId);

    /**
     * Busca tarefas cujo título contenha o texto informado (case-insensitive).
     */
    List<Task> findByTitleContainingIgnoreCase(String title);
}
