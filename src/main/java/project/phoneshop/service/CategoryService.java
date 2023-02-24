package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.CategoryEntity;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface CategoryService {
    CategoryEntity saveCategory(CategoryEntity category);

    List<CategoryEntity> foundCategory(CategoryEntity category);

    List<CategoryEntity> findAllCategory();

    List<CategoryEntity> findByCategoryParent(UUID uuid);

    List<CategoryEntity> findCategoryRoot();

    CategoryEntity findById(UUID id);

    List<CategoryEntity> findCategoryChild(CategoryEntity category);

    void deleteCategory(UUID id);
}
