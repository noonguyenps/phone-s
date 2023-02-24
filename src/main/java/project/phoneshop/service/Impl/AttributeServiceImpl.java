package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.AttributeEntity;
import project.phoneshop.model.entity.AttributeOptionEntity;
import project.phoneshop.model.entity.CategoryEntity;
import project.phoneshop.repository.AttributeOptionRepository;
import project.phoneshop.repository.AttributeRepository;
import project.phoneshop.service.AttributeService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {
    private final AttributeRepository attributeRepository;
    private final AttributeOptionRepository attributeOptionRepository;

    @Override
    public AttributeEntity findById(String id) {
        Optional<AttributeEntity> attribute = attributeRepository.findById(id);
        if (attribute.isEmpty())
            return null;
        return attribute.get();
    }
    @Override
    public  AttributeOptionEntity findByIdAttributeOption(String id){
        Optional<AttributeOptionEntity> attributeOption = attributeOptionRepository.findById(id);
        if(attributeOption.isEmpty())
            return null;
        return attributeOption.get();
    }

    @Override
    public List<AttributeEntity> findAllAttribute(){
        List<AttributeEntity> list = attributeRepository.findAll();
        return list;
    }

    @Override
    public List<AttributeOptionEntity> findAllAttributeOption() {
        List<AttributeOptionEntity> list = attributeOptionRepository.findAll();
        return list;
    }

    @Override
    public AttributeEntity saveAttribute(AttributeEntity attributeEntity) {
        return attributeRepository.save(attributeEntity);
    }
    @Override
    public void deleteAttribute(String id){
        attributeRepository.deleteById(id);
    }

    @Override
    public List<AttributeEntity> findByCategory(CategoryEntity category) {
        return null;
    }
    @Override
    public AttributeOptionEntity saveAttributeOption(AttributeOptionEntity attributeOption){
        return attributeOptionRepository.save(attributeOption);
    }

}
