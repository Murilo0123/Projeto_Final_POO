package com.faculdade.taskmanager.controller;

import com.faculdade.taskmanager.dto.TaskRequestDTO;
import com.faculdade.taskmanager.dto.TaskResponseDTO;
import com.faculdade.taskmanager.model.TaskStatus;
import com.faculdade.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciamento de tarefas.
 * Expõe 7 rotas com os verbos HTTP: GET, POST, PUT, PATCH, DELETE.
 */
@RestController
@RequestMapping("/tasks")
@Tag(name = "Tarefas", description = "Operações de gerenciamento de tarefas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // ============================================================
    // POST /tasks — Criar nova tarefa
    // ============================================================
    @PostMapping
    @Operation(summary = "Criar tarefa", description = "Cria uma nova tarefa no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
        @ApiResponse(responseCode = "422", description = "Violação de regra de negócio", content = @Content)
    })
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskRequestDTO dto) {
        TaskResponseDTO response = taskService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // GET /tasks — Listar todas as tarefas
    // ============================================================
    @GetMapping
    @Operation(summary = "Listar tarefas", description = "Retorna todas as tarefas cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<TaskResponseDTO>> findAll() {
        return ResponseEntity.ok(taskService.findAll());
    }

    // ============================================================
    // GET /tasks/{id} — Buscar tarefa por ID
    // ============================================================
    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por ID", description = "Retorna uma tarefa específica pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    public ResponseEntity<TaskResponseDTO> findById(
            @Parameter(description = "ID da tarefa", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    // ============================================================
    // PUT /tasks/{id} — Atualizar tarefa completa
    // ============================================================
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa", description = "Atualiza todos os dados de uma tarefa existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    public ResponseEntity<TaskResponseDTO> update(
            @Parameter(description = "ID da tarefa", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO dto) {
        return ResponseEntity.ok(taskService.update(id, dto));
    }

    // ============================================================
    // PATCH /tasks/{id}/status — Atualizar apenas o status
    // ============================================================
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da tarefa",
               description = "Atualiza somente o status de uma tarefa. Tarefas CANCELLED não podem ser reativadas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content),
        @ApiResponse(responseCode = "422", description = "Operação inválida (ex: reativar tarefa cancelada)", content = @Content)
    })
    public ResponseEntity<TaskResponseDTO> updateStatus(
            @Parameter(description = "ID da tarefa", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novo status", example = "IN_PROGRESS")
            @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    // ============================================================
    // DELETE /tasks/{id} — Deletar tarefa
    // ============================================================
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tarefa", description = "Remove uma tarefa do sistema pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tarefa removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da tarefa", example = "1")
            @PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // GET /tasks/filter — Filtrar tarefas por status (critério)
    // ============================================================
    @GetMapping("/filter")
    @Operation(summary = "Filtrar tarefas por status",
               description = "Retorna tarefas filtradas pelo status informado como query parameter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarefas filtradas retornadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Status inválido", content = @Content)
    })
    public ResponseEntity<List<TaskResponseDTO>> findByStatus(
            @Parameter(description = "Status para filtro", example = "PENDING")
            @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.findByStatus(status));
    }
}
