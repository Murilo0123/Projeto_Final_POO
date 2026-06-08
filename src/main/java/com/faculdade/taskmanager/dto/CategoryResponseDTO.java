package com.faculdade.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * DTO de saída com os dados de uma categoria retornados pela API.
 */
@Data
@Builder
@Schema(description = "Dados de uma categoria retornados pela API")
public class CategoryResponseDTO {

    @Schema(description = "ID único da categoria", example = "1")
    private Long id;

    @Schema(description = "Nome da categoria", example = "Estudos")
    private String name;

    @Schema(description = "Descrição da categoria", example = "Tarefas relacionadas a estudos acadêmicos")
    private String description;

    @Schema(description = "Quantidade de tarefas nesta categoria", example = "5")
    private int taskCount;
}
