package ru.practicum.explorewithme.main.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.category.model.Category;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PatchMapping("/admin/categories")
    public Category updateCategory(@RequestBody @Valid Category categoryDto) {
        log.info("Изменить категорию {}", categoryDto.toString());
        return categoryService.updateCategory(categoryDto);
    }

    @PostMapping("/admin/categories")
    public Category addCategory(@RequestBody @Valid Category categoryDto) {
        log.info("Добавить категорию {}", categoryDto);
        return categoryService.addCategory(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteCategoryById(@PathVariable Long catId) {
        log.info("Удалить категорию с id: {}", catId);
        categoryService.deleteCategoryById(catId);
    }
}
