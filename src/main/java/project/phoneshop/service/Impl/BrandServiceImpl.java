package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.BrandEntity;
import project.phoneshop.repository.BrandRepository;
import project.phoneshop.service.BrandService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    @Override
    public BrandEntity findById(UUID id) {
        Optional<BrandEntity> brand = brandRepository.findById(id);
        if(brand.isEmpty())
            return null;
        return brand.get();
    }
    @Override
    public BrandEntity saveBrand(BrandEntity brand) {
        return brandRepository.save(brand);
    }
    @Override
    public List<BrandEntity> findAll(int page, int size){
        Pageable paging = PageRequest.of(page, size);
        Page<BrandEntity> pagedResult = brandRepository.findAll(paging);
        return pagedResult.toList();
    }
    @Override
    public void deleteBrand(UUID id){
        brandRepository.deleteById(id);
    }
}
