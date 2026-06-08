package com.faculdade.taskmanager.repository;

import com.faculdade.taskmanager.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório JPA para a entidade Category.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Busca uma categoria pelo nome (case-insensitive).
     */
    Optional<Category> findByNameIgnoreCase(String name);

    /**
     * Verifica se já existe uma categoria com determinado nome.
     */
    boolean existsByNameIgnoreCase(String name);
}
