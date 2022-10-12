package ru.practicum.explorewithme.main.category;

import ru.practicum.explorewithme.main.category.model.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category categoryDto);

    Category updateCategory(Category categoryDto);

    List<Category> findAll(Integer from, Integer size);

    Category findById(Long id);

    void deleteCategoryById(Long id);
}
