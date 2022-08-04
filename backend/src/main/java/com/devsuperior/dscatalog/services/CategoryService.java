package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(x -> new CategoryDto(x)).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(Long id) {
        Optional<Category> catOpt = categoryRepository.findById(id);
        Category category = catOpt.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada!"));
        return new CategoryDto(catOpt.get());
    }

    @Transactional
    public CategoryDto insert(CategoryDto catDto) {
        Category category = new Category();
        category.setName(catDto.getName());
        category = categoryRepository.save(category);
        return new CategoryDto(category);
    }

    @Transactional
    public CategoryDto update(Long id, CategoryDto catDto) {
        try {
            Category category = categoryRepository.getReferenceById(id);
            category.setName(catDto.getName());
            category = categoryRepository.save(category);
            return new CategoryDto(category);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("O id: " + id + " não foi encontrado.");
        }
    }
}
