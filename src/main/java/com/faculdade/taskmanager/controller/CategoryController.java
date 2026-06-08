package com.faculdade.taskmanager.controller;

import com.faculdade.taskmanager.dto.CategoryRequestDTO;
import com.faculdade.taskmanager.dto.CategoryResponseDTO;
import com.faculdade.taskmanager.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciamento de categorias.
 */
@RestController
@RequestMapping("/categories")
@Tag(name = "Categorias", description = "Operações de gerenciamento de categorias de tarefas")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "422", description = "Nome de categoria já existente", content = @Content)
    })
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Listar categorias", description = "Retorna todas as categorias cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    })
    public ResponseEntity<CategoryResponseDTO> findById(
            @Parameter(description = "ID da categoria") @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoria atualizada"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
        @ApiResponse(responseCode = "422", description = "Nome já em uso", content = @Content)
    })
    public ResponseEntity<CategoryResponseDTO> update(
            @Parameter(description = "ID da categoria") @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.ok(categoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar categoria",
               description = "Remove uma categoria. Não é possível remover categorias com tarefas vinculadas.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoria removida"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
        @ApiResponse(responseCode = "422", description = "Categoria possui tarefas vinculadas", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da categoria") @PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
