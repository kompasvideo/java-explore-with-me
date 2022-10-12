package ru.practicum.explorewithme.main.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.category.model.Category;
import ru.practicum.explorewithme.main.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category addCategory(Category categoryDto) {
        return categoryRepository.save(categoryDto);
    }

    @Override
    @Transactional
    public Category updateCategory(Category categoryDto) {
        if (categoryRepository.existsById(categoryDto.getId()))
            return categoryRepository.save(categoryDto);
        else throw new NotFoundException("NotFoundException CategoryServiceImpl updateCategory, id: " +
            categoryDto.getId());
    }

    @Override
    public List<Category> findAll(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from, size)).toList();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
            new NotFoundException("NotFoundException CategoryServiceImpl findById, id: " + id));
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        if (categoryRepository.existsById(id))
            categoryRepository.deleteById(id);
        else throw new NotFoundException("NotFoundException CategoryServiceImpl deleteCategoryById, id: " + id);
    }
}
