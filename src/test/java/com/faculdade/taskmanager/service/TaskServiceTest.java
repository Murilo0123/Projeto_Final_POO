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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários - TaskService")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskRequestDTO requestDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Estudos")
                .description("Tarefas de estudo")
                .build();

        task = Task.builder()
                .id(1L)
                .title("Estudar Spring Boot")
                .description("Revisar JPA")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(10))
                .createdAt(LocalDateTime.now())
                .category(category)
                .build();

        requestDTO = new TaskRequestDTO();
        requestDTO.setTitle("Estudar Spring Boot");
        requestDTO.setDescription("Revisar JPA");
        requestDTO.setStatus(TaskStatus.PENDING);
        requestDTO.setDueDate(LocalDate.now().plusDays(10));
        requestDTO.setCategoryId(1L);
    }

    // ===================== CREATE =====================

    @Test
    @DisplayName("create: deve criar tarefa com sucesso")
    void create_success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO result = taskService.create(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Estudar Spring Boot");
        assertThat(result.getCategoryName()).isEqualTo("Estudos");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("create: deve criar tarefa sem categoria quando categoryId é null")
    void create_withoutCategory() {
        requestDTO.setCategoryId(null);
        task.setCategory(null);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO result = taskService.create(requestDTO);

        assertThat(result.getCategoryId()).isNull();
        verify(categoryRepository, never()).findById(any());
    }

    @Test
    @DisplayName("create: deve lançar ResourceNotFoundException se categoria não existe")
    void create_categoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.create(requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Categoria com ID 1 não encontrada");
        verify(taskRepository, never()).save(any());
    }

    // ===================== FIND ALL =====================

    @Test
    @DisplayName("findAll: deve retornar lista de tarefas")
    void findAll_success() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskResponseDTO> result = taskService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Estudar Spring Boot");
    }

    @Test
    @DisplayName("findAll: deve retornar lista vazia quando não há tarefas")
    void findAll_empty() {
        when(taskRepository.findAll()).thenReturn(List.of());

        List<TaskResponseDTO> result = taskService.findAll();

        assertThat(result).isEmpty();
    }

    // ===================== FIND BY ID =====================

    @Test
    @DisplayName("findById: deve retornar tarefa quando ID existe")
    void findById_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponseDTO result = taskService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Estudar Spring Boot");
    }

    @Test
    @DisplayName("findById: deve lançar ResourceNotFoundException quando ID não existe")
    void findById_notFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Tarefa com ID 99 não encontrada");
    }

    // ===================== UPDATE =====================

    @Test
    @DisplayName("update: deve atualizar tarefa com sucesso")
    void update_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO result = taskService.update(1L, requestDTO);

        assertThat(result).isNotNull();
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("update: deve lançar ResourceNotFoundException quando tarefa não existe")
    void update_taskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.update(99L, requestDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ===================== UPDATE STATUS =====================

    @Test
    @DisplayName("updateStatus: deve atualizar status com sucesso")
    void updateStatus_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        task.setStatus(TaskStatus.IN_PROGRESS);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO result = taskService.updateStatus(1L, TaskStatus.IN_PROGRESS);

        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("updateStatus: deve lançar BusinessException ao reativar tarefa cancelada")
    void updateStatus_cannotReactivateCancelled() {
        task.setStatus(TaskStatus.CANCELLED);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.updateStatus(1L, TaskStatus.PENDING))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("canceladas não podem ser reativadas");
    }

    @Test
    @DisplayName("updateStatus: deve permitir manter CANCELLED em CANCELLED")
    void updateStatus_cancelledToCancel_allowed() {
        task.setStatus(TaskStatus.CANCELLED);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        assertThatCode(() -> taskService.updateStatus(1L, TaskStatus.CANCELLED))
                .doesNotThrowAnyException();
    }

    // ===================== DELETE =====================

    @Test
    @DisplayName("delete: deve remover tarefa com sucesso")
    void delete_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        assertThatCode(() -> taskService.delete(1L)).doesNotThrowAnyException();
        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("delete: deve lançar ResourceNotFoundException quando tarefa não existe")
    void delete_notFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(taskRepository, never()).delete(any());
    }

    // ===================== FIND BY STATUS =====================

    @Test
    @DisplayName("findByStatus: deve retornar tarefas filtradas pelo status")
    void findByStatus_success() {
        when(taskRepository.findByStatus(TaskStatus.PENDING)).thenReturn(List.of(task));

        List<TaskResponseDTO> result = taskService.findByStatus(TaskStatus.PENDING);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(TaskStatus.PENDING);
    }
}
