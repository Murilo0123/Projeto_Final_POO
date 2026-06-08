package com.faculdade.taskmanager.controller;

import com.faculdade.taskmanager.dto.TaskRequestDTO;
import com.faculdade.taskmanager.dto.TaskResponseDTO;
import com.faculdade.taskmanager.exception.ResourceNotFoundException;
import com.faculdade.taskmanager.model.TaskStatus;
import com.faculdade.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@DisplayName("Testes unitários - TaskController")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private ObjectMapper objectMapper;
    private TaskResponseDTO responseDTO;
    private TaskRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        responseDTO = TaskResponseDTO.builder()
                .id(1L)
                .title("Estudar Spring Boot")
                .description("Revisar JPA")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(10))
                .createdAt(LocalDateTime.now())
                .categoryId(1L)
                .categoryName("Estudos")
                .build();

        requestDTO = new TaskRequestDTO();
        requestDTO.setTitle("Estudar Spring Boot");
        requestDTO.setDescription("Revisar JPA");
        requestDTO.setStatus(TaskStatus.PENDING);
        requestDTO.setDueDate(LocalDate.now().plusDays(10));
        requestDTO.setCategoryId(1L);
    }

    @Test
    @DisplayName("POST /tasks: deve retornar 201 ao criar tarefa válida")
    void create_shouldReturn201() throws Exception {
        when(taskService.create(any(TaskRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Estudar Spring Boot"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("POST /tasks: deve retornar 400 quando título está em branco")
    void create_shouldReturn400WhenTitleBlank() throws Exception {
        requestDTO.setTitle("");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /tasks: deve retornar lista de tarefas com 200")
    void findAll_shouldReturn200() throws Exception {
        when(taskService.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Estudar Spring Boot"));
    }

    @Test
    @DisplayName("GET /tasks/{id}: deve retornar 200 quando tarefa existe")
    void findById_shouldReturn200() throws Exception {
        when(taskService.findById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /tasks/{id}: deve retornar 404 quando tarefa não existe")
    void findById_shouldReturn404() throws Exception {
        when(taskService.findById(99L)).thenThrow(new ResourceNotFoundException("Tarefa com ID 99 não encontrada."));

        mockMvc.perform(get("/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Recurso Não Encontrado"));
    }

    @Test
    @DisplayName("PUT /tasks/{id}: deve retornar 200 ao atualizar tarefa")
    void update_shouldReturn200() throws Exception {
        when(taskService.update(eq(1L), any(TaskRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Estudar Spring Boot"));
    }

    @Test
    @DisplayName("PATCH /tasks/{id}/status: deve retornar 200 ao atualizar status")
    void updateStatus_shouldReturn200() throws Exception {
        responseDTO = TaskResponseDTO.builder()
                .id(1L).title("Estudar Spring Boot").status(TaskStatus.IN_PROGRESS)
                .dueDate(LocalDate.now().plusDays(5)).createdAt(LocalDateTime.now()).build();

        when(taskService.updateStatus(1L, TaskStatus.IN_PROGRESS)).thenReturn(responseDTO);

        mockMvc.perform(patch("/tasks/1/status")
                        .param("status", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("DELETE /tasks/{id}: deve retornar 204 ao deletar tarefa")
    void delete_shouldReturn204() throws Exception {
        doNothing().when(taskService).delete(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /tasks/{id}: deve retornar 404 se tarefa não existe")
    void delete_shouldReturn404() throws Exception {
        doThrow(new ResourceNotFoundException("Tarefa com ID 99 não encontrada."))
                .when(taskService).delete(99L);

        mockMvc.perform(delete("/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /tasks/filter: deve retornar 200 com tarefas filtradas por status")
    void findByStatus_shouldReturn200() throws Exception {
        when(taskService.findByStatus(TaskStatus.PENDING)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/tasks/filter").param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}
