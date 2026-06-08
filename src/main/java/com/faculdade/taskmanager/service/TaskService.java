package com.faculdade.taskmanager.service;

import com.faculdade.taskmanager.dto.TaskRequestDTO;
import com.faculdade.taskmanager.dto.TaskResponseDTO;
import com.faculdade.taskmanager.exception.BusinessException;
import com.faculdade.taskmanager.exception.ResourceNotFoundException;
import com.faculdade.taskmanager.model.Category;
import com.faculdade.taskmanager.model.Task;
import com.faculdade.taskmanager.model.TaskStatus;
import com.faculdade.taskmanager.repository.CategoryRepository;
import com.faculdade.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Camada de serviço responsável pelas regras de negócio das tarefas.
 * Usa injeção de dependências via construtor (boas práticas de POO).
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    // Injeção via construtor (baixo acoplamento)
    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Cria uma nova tarefa.
     */
    @Transactional
    public TaskResponseDTO create(TaskRequestDTO dto) {
        Task task = buildTaskFromDTO(dto, new Task());
        Task saved = taskRepository.save(task);
        return toResponseDTO(saved);
    }

    /**
     * Lista todas as tarefas.
     */
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca uma tarefa pelo ID.
     */
    @Transactional(readOnly = true)
    public TaskResponseDTO findById(Long id) {
        Task task = findTaskOrThrow(id);
        return toResponseDTO(task);
    }

    /**
     * Atualiza uma tarefa existente.
     */
    @Transactional
    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        Task task = findTaskOrThrow(id);
        buildTaskFromDTO(dto, task);
        Task updated = taskRepository.save(task);
        return toResponseDTO(updated);
    }

    /**
     * Atualiza apenas o status de uma tarefa (PATCH).
     */
    @Transactional
    public TaskResponseDTO updateStatus(Long id, TaskStatus newStatus) {
        Task task = findTaskOrThrow(id);

        // Regra de negócio: tarefa CANCELLED não pode ser reativada
        if (task.getStatus() == TaskStatus.CANCELLED && newStatus != TaskStatus.CANCELLED) {
            throw new BusinessException("Tarefas canceladas não podem ser reativadas.");
        }

        task.setStatus(newStatus);
        Task updated = taskRepository.save(task);
        return toResponseDTO(updated);
    }

    /**
     * Remove uma tarefa pelo ID.
     */
    @Transactional
    public void delete(Long id) {
        Task task = findTaskOrThrow(id);
        taskRepository.delete(task);
    }

    /**
     * Filtra tarefas pelo status.
     */
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // =====================
    // Métodos auxiliares privados
    // =====================

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa com ID " + id + " não encontrada."));
    }

    private Task buildTaskFromDTO(TaskRequestDTO dto, Task task) {
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoria com ID " + dto.getCategoryId() + " não encontrada."));
            task.setCategory(category);
        } else {
            task.setCategory(null);
        }

        return task;
    }

    private TaskResponseDTO toResponseDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .categoryId(task.getCategory() != null ? task.getCategory().getId() : null)
                .categoryName(task.getCategory() != null ? task.getCategory().getName() : null)
                .build();
    }
}
