package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.BrandEntity;
import project.phoneshop.model.entity.ProductEntity;
import project.phoneshop.repository.BrandRepository;
import project.phoneshop.service.BrandService;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

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
    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');
    }
    @Override
    public List<BrandEntity> findBrandByKeyword(String keyword, int pageNo, int pageSize, String sort){
        Pageable paging = null;
        keyword = removeAccent(keyword);
        String[] arrOfStr = keyword.split(" ");
        Page<BrandEntity> pageResult;
        if(arrOfStr.length > 1){
            pageResult = brandRepository.findByKeyword(arrOfStr[0].toLowerCase(), arrOfStr[1].toLowerCase(), paging);
        }
        else{
            pageResult = brandRepository.findByKeyword(arrOfStr[0].toLowerCase(), "hfladsfjskjafkkjsadf", paging);
        }
        return pageResult.toList();
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
