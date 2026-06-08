package com.faculdade.taskmanager.service;

import com.faculdade.taskmanager.dto.CategoryRequestDTO;
import com.faculdade.taskmanager.dto.CategoryResponseDTO;
import com.faculdade.taskmanager.exception.BusinessException;
import com.faculdade.taskmanager.exception.ResourceNotFoundException;
import com.faculdade.taskmanager.model.Category;
import com.faculdade.taskmanager.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários - CategoryService")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Estudos")
                .description("Tarefas acadêmicas")
                .tasks(List.of())
                .build();

        requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Estudos");
        requestDTO.setDescription("Tarefas acadêmicas");
    }

    @Test
    @DisplayName("create: deve criar categoria com sucesso")
    void create_success() {
        when(categoryRepository.existsByNameIgnoreCase("Estudos")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO result = categoryService.create(requestDTO);

        assertThat(result.getName()).isEqualTo("Estudos");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("create: deve lançar BusinessException se nome já existe")
    void create_duplicateName() {
        when(categoryRepository.existsByNameIgnoreCase("Estudos")).thenReturn(true);

        assertThatThrownBy(() -> categoryService.create(requestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Já existe uma categoria com o nome 'Estudos'");
    }

    @Test
    @DisplayName("findAll: deve retornar lista de categorias")
    void findAll_success() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryResponseDTO> result = categoryService.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("findById: deve retornar categoria quando ID existe")
    void findById_success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponseDTO result = categoryService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findById: deve lançar ResourceNotFoundException quando ID não existe")
    void findById_notFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("update: deve atualizar categoria com sucesso")
    void update_success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findByNameIgnoreCase("Estudos")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO result = categoryService.update(1L, requestDTO);

        assertThat(result.getName()).isEqualTo("Estudos");
    }

    @Test
    @DisplayName("update: deve lançar BusinessException se nome já usado por outra categoria")
    void update_nameConflict() {
        Category other = Category.builder().id(2L).name("Estudos").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findByNameIgnoreCase("Estudos")).thenReturn(Optional.of(other));

        assertThatThrownBy(() -> categoryService.update(1L, requestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Já existe uma categoria com o nome 'Estudos'");
    }

    @Test
    @DisplayName("delete: deve remover categoria sem tarefas")
    void delete_success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        assertThatCode(() -> categoryService.delete(1L)).doesNotThrowAnyException();
        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("delete: deve lançar BusinessException se categoria possui tarefas")
    void delete_withTasks() {
        com.faculdade.taskmanager.model.Task task = com.faculdade.taskmanager.model.Task.builder()
                .id(1L).title("Tarefa").build();
        category.setTasks(List.of(task));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> categoryService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("possui tarefas vinculadas");
    }

    @Test
    @DisplayName("delete: deve lançar ResourceNotFoundException quando ID não existe")
    void delete_notFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
