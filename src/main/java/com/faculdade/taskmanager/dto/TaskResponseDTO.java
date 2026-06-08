package com.faculdade.taskmanager.dto;

import com.faculdade.taskmanager.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de saída com os dados de uma tarefa retornados pela API.
 */
@Data
@Builder
@Schema(description = "Dados de uma tarefa retornados pela API")
public class TaskResponseDTO {

    @Schema(description = "ID único da tarefa", example = "1")
    private Long id;

    @Schema(description = "Título da tarefa", example = "Estudar Spring Boot")
    private String title;

    @Schema(description = "Descrição da tarefa", example = "Revisar capítulos de JPA e testes")
    private String description;

    @Schema(description = "Status atual da tarefa", example = "PENDING")
    private TaskStatus status;

    @Schema(description = "Data de vencimento", example = "2025-12-31")
    private LocalDate dueDate;

    @Schema(description = "Data e hora de criação", example = "2024-01-10T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "ID da categoria", example = "1")
    private Long categoryId;

    @Schema(description = "Nome da categoria", example = "Estudos")
    private String categoryName;
}
