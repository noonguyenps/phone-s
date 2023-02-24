package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.BrandEntity;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface BrandService {
    BrandEntity findById(UUID id);

    BrandEntity saveBrand(BrandEntity brand);

    List<BrandEntity> findAll(int page, int size);

    void deleteBrand(UUID id);
}
