package com.faculdade.taskmanager.service;

import com.faculdade.taskmanager.dto.CategoryRequestDTO;
import com.faculdade.taskmanager.dto.CategoryResponseDTO;
import com.faculdade.taskmanager.exception.BusinessException;
import com.faculdade.taskmanager.exception.ResourceNotFoundException;
import com.faculdade.taskmanager.model.Category;
import com.faculdade.taskmanager.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Camada de serviço para gerenciamento de categorias.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new BusinessException("Já existe uma categoria com o nome '" + dto.getName() + "'.");
        }

        Category category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        Category saved = categoryRepository.save(category);
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id) {
        Category category = findCategoryOrThrow(id);
        return toResponseDTO(category);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = findCategoryOrThrow(id);

        // Verifica se outro registro já usa esse nome
        categoryRepository.findByNameIgnoreCase(dto.getName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("Já existe uma categoria com o nome '" + dto.getName() + "'.");
                });

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        Category updated = categoryRepository.save(category);
        return toResponseDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        Category category = findCategoryOrThrow(id);
        if (category.getTasks() != null && !category.getTasks().isEmpty()) {
            throw new BusinessException("Não é possível excluir uma categoria que possui tarefas vinculadas.");
        }
        categoryRepository.delete(category);
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria com ID " + id + " não encontrada."));
    }

    private CategoryResponseDTO toResponseDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .taskCount(category.getTasks() != null ? category.getTasks().size() : 0)
                .build();
    }
}
