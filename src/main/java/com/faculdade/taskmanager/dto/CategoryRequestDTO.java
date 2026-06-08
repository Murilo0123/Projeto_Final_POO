package com.faculdade.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para criação e atualização de categorias.
 */
@Data
@Schema(description = "Payload para criação ou atualização de uma categoria")
public class CategoryRequestDTO {

    @NotBlank(message = "O nome da categoria é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Nome da categoria", example = "Estudos", required = true)
    private String name;

    @Size(max = 300, message = "A descrição deve ter no máximo 300 caracteres")
    @Schema(description = "Descrição da categoria", example = "Tarefas relacionadas a estudos acadêmicos")
    private String description;
}
