package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.AttributeEntity;
import project.phoneshop.model.entity.AttributeOptionEntity;
import project.phoneshop.model.entity.CategoryEntity;

import java.util.List;

@Component
@Service
public interface AttributeService {
    AttributeOptionEntity findByIdAttributeOption(String id);

    List<AttributeEntity> findAllAttribute();

    List<AttributeOptionEntity> findAllAttributeOption();

    AttributeEntity findById(String id);

    AttributeEntity saveAttribute(AttributeEntity attributeEntity);

    void deleteAttribute(String id);

    List<AttributeEntity> findByCategory(CategoryEntity category);

    AttributeOptionEntity saveAttributeOption(AttributeOptionEntity attributeOption);
}
