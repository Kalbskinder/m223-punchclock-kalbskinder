package ch.zli.m223.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import ch.zli.m223.dto.CategoryDto;
import ch.zli.m223.model.Category;
import ch.zli.m223.repository.CategoryRepository;
import ch.zli.m223.repository.EntryRepository;

@ApplicationScoped
public class CategoryService {
    @Inject
    CategoryRepository categoryRepository;

    @Inject
    EntryRepository entryRepository;

    public List<CategoryDto> findAll() {
        return categoryRepository.listAll().stream()
                .map(Category::toDto)
                .toList();
    }

    public CategoryDto findById(Long id) {
        return getCategory(id).toDto();
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto dto) {
        Category category = dto.toEntity();
        categoryRepository.persist(category);
        return category.toDto();
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = getCategory(id);
        category.setTitle(dto.getTitle());
        return category.toDto();
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategory(id);
        // Entries keep existing, they just lose their category.
        entryRepository.update("category = null where category = ?1", category);
        categoryRepository.delete(category);
    }

    Category getCategory(Long id) {
        return categoryRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found."));
    }
}
