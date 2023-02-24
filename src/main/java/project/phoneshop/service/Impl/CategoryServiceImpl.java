package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.CategoryEntity;
import project.phoneshop.repository.CategoryRepository;
import project.phoneshop.service.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    final CategoryRepository categoryRepository;

    @Override
    public CategoryEntity saveCategory(CategoryEntity category) {
        return categoryRepository.save(category);
    }
    @Override
    public List<CategoryEntity> foundCategory(CategoryEntity category){
        return categoryRepository.findByName(category.getName().trim());
    }
    @Override
    public List<CategoryEntity> findAllCategory(){
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        return categoryEntityList;
    }
    @Override
    public List<CategoryEntity> findByCategoryParent(UUID uuid){
        return null;
    }
    @Override
    public List<CategoryEntity> findCategoryRoot(){
        List<CategoryEntity> categoryEntityList = categoryRepository.findCategoryRoot();
        return categoryEntityList;
    }
    @Override
    public CategoryEntity findById(UUID id){
        Optional<CategoryEntity> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            return null;
        }
        return category.get();
    }
    @Override
    public List<CategoryEntity> findCategoryChild(CategoryEntity category){
        List<CategoryEntity> categoryEntityList = categoryRepository.findCategoryChild(category.getId());
        if (categoryEntityList.isEmpty())
            return null;
        return categoryEntityList;
    }
    @Override
    public void deleteCategory(UUID id){
        categoryRepository.deleteById(id);
    }

}
