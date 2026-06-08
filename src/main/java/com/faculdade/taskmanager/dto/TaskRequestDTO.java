package com.faculdade.taskmanager.dto;

import com.faculdade.taskmanager.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de entrada para criação e atualização de tarefas.
 * Contém validações com Bean Validation.
 */
@Data
@Schema(description = "Payload para criação ou atualização de uma tarefa")
public class TaskRequestDTO {

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 150, message = "O título deve ter entre 3 e 150 caracteres")
    @Schema(description = "Título da tarefa", example = "Estudar Spring Boot", required = true)
    private String title;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    @Schema(description = "Descrição detalhada da tarefa", example = "Revisar capítulos de JPA e testes")
    private String description;

    @NotNull(message = "O status é obrigatório")
    @Schema(description = "Status atual da tarefa", example = "PENDING", required = true)
    private TaskStatus status;

    @NotNull(message = "A data de vencimento é obrigatória")
    @Future(message = "A data de vencimento deve ser uma data futura")
    @Schema(description = "Data de vencimento da tarefa (futura)", example = "2025-12-31", required = true)
    private LocalDate dueDate;

    @Schema(description = "ID da categoria da tarefa (opcional)", example = "1")
    private Long categoryId;
}
