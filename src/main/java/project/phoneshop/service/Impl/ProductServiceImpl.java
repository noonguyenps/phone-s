package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.*;
import project.phoneshop.model.payload.response.product.ProductResponse;
import project.phoneshop.repository.AttributeOptionRepository;
import project.phoneshop.repository.ImageProductRepository;
import project.phoneshop.repository.ProductRepository;
import project.phoneshop.service.ProductRatingService;
import project.phoneshop.service.ProductService;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final AttributeOptionRepository attributeOptionRepository;
    private final ImageProductRepository imageProductRepository;
    private final ProductRatingService productRatingService;
    @Override
    public List<ProductEntity> findAllProduct(){
        List<ProductEntity> productEntityList = productRepository.findAll();
        return productEntityList;
    }
    @Override
    public ProductEntity saveProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    @Override
    public ProductEntity findById(UUID id){
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        if(productEntity.isEmpty()){
            return null;
        }
        return productEntity.get();
    }
    @Override
    public void deleteProduct(UUID id){
        productRepository.deleteById(id);
    }
    @Override
    public void saveListImageProduct(List<String> listUrl, ProductEntity product){
        for (String url : listUrl){
            ImageProductEntity imageProductEntity = new ImageProductEntity();
            imageProductEntity.setUrl(url);
            imageProductEntity.setProduct(product);
            imageProductRepository.save(imageProductEntity);
        }
    }
    @Override
    public void deleteListImgProduct(ProductEntity product){
        List<ImageProductEntity> imageProductEntityList = imageProductRepository.findByProduct(product);
        for (ImageProductEntity imageProductEntity : imageProductEntityList){
            imageProductRepository.delete(imageProductEntity);
        }
    }

    @Override
    public void addAttribute(ProductEntity product, String attributeOptionId){
//        Optional<AttributeOptionEntity> attribute = attributeOptionRepository.findById(attributeOptionId);
//        product.getAttributeOptionEntities().add(attribute.get());
        productRepository.save(product);
    }
    @Override
    public void deleteAttribute(ProductEntity product, String attributeId){
//        Optional<AttributeOptionEntity> attribute = attributeOptionRepository.findById(attributeId);
//        product.getAttributeOptionEntities().remove(attribute.get());
        productRepository.save(product);
    }

    @Override
    public List<ProductEntity> findPaginated(int pageNo, int pageSize, String sort) {
        Pageable paging = null;
        switch (sort){
            case "product_price_up" : paging = PageRequest.of(pageNo, pageSize, Sort.by("product_price").descending());break;
            case "product_price_down" : paging = PageRequest.of(pageNo, pageSize, Sort.by("product_price").ascending());break;
            default : paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        }
        Page<ProductEntity> pagedResult = productRepository.findAllProduct(paging);
        return pagedResult.toList();
    }
    @Override
    public List<ProductEntity> findByProductStatus(int page, int size, int status){
        Pageable paging = PageRequest.of(page, size);
        Page<ProductEntity> pagedResult = productRepository.findAllProductByStatus(status,paging);
        return pagedResult.toList();
    }
    @Override
    public List<ProductEntity> findProductByCategory(CategoryEntity category, int pageNo, int pageSize, String sort){
        Pageable paging = null;
        switch (sort){
            case "product_price_up" : paging = PageRequest.of(pageNo, pageSize, Sort.by("product_price").descending());break;
            case "product_price_down" : paging = PageRequest.of(pageNo, pageSize, Sort.by("product_price").ascending());break;
            default : paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        }
        List<UUID> listCategory = new ArrayList<>();
        getAllCategory(category,listCategory);
        Page<ProductEntity> pagedResult = productRepository.findByCategory(listCategory,paging);
        return pagedResult.toList();
    }
    public void getAllCategory(CategoryEntity category, List<UUID> categoryEntities){
        if(category.getCategoryEntities() != null){
            categoryEntities.add(category.getId());
            for (CategoryEntity categoryEntity : category.getCategoryEntities())
                getAllCategory(categoryEntity,categoryEntities);
        }else
            categoryEntities.add(category.getId());
    }
    @Override
    public List<ProductEntity> findProductByBrand(BrandEntity brand, int pageNo, int pageSize, String sort){
        Pageable paging = null;
        switch (sort){
            case "product_price_up" : paging = PageRequest.of(pageNo, pageSize, Sort.by("product_price").descending());break;
            case "product_price_down" : paging = PageRequest.of(pageNo, pageSize, Sort.by("product_price").ascending());break;
            default : paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        }
        Page<ProductEntity> pagedResult = productRepository.findByBrand(brand.getId(),paging);
        return pagedResult.toList();
    }
    @Override
    public List<ProductEntity> findProductByAttributes(CategoryEntity category, List<String> listAttribute,int pageNo, int pageSize, String sort){
        Pageable paging = null;
        switch (sort){
            case "product_price_up" : paging = PageRequest.of(pageNo, pageSize, Sort.by("product_price").descending());break;
            case "product_price_down" : paging = PageRequest.of(pageNo, pageSize, Sort.by("product_price").ascending());break;
            default : paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        }
        List<UUID> listCategory = new ArrayList<>();
        getAllCategory(category,listCategory);
        Page<ProductEntity> pageResult = productRepository.findByAttributes(listCategory,listAttribute,paging);
        return pageResult.toList();
    }
    @Override
    public List<ProductEntity> findProductByKeyword(String keyword,int pageNo, int pageSize, String sort){
        Pageable paging = null;
        switch (sort){
            case "product_price_up" : paging = PageRequest.of(pageNo, pageSize, Sort.by("product_price").descending());break;
            case "product_price_down" : paging = PageRequest.of(pageNo, pageSize, Sort.by("product_price").ascending());break;
            default : paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        }
        keyword = removeAccent(keyword);
        String[] arrOfStr = keyword.split(" ");
        Page<ProductEntity> pageResult;
        if(arrOfStr.length > 1){
            pageResult = productRepository.findByKeyword(arrOfStr[0].toLowerCase(), arrOfStr[1].toLowerCase(), paging);
        }
        else{
            pageResult = productRepository.findByKeyword(arrOfStr[0].toLowerCase(), "hfladsfjskjafkkjsadf", paging);
        }
        return pageResult.toList();
    }
    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');
    }
    @Override
    public List<ProductEntity> searchByBrand(BrandEntity brand){
        return brand.getListProduct();
    }
    @Override
    public void minusProduct(ProductEntity product, int quantity){
        product.setInventory(product.getInventory()-quantity);
        product.setSellAmount(product.getSellAmount() + quantity);
        productRepository.save(product);
    }
    @Override
    public long countProduct() {
        return productRepository.count();
    }
    @Override
    public ProductResponse productResponse(ProductEntity product){
        List<ImageProductEntity> imageProductEntityList = imageProductRepository.findByProduct(product);
        List<String> list = new ArrayList<>();
        for (ImageProductEntity imageProductEntity : imageProductEntityList){
            list.add(imageProductEntity.getUrl());
        }
        String url = "https://res.cloudinary.com/duk2lo18t/image/upload/v1667887284/frontend/R_zzr2lm.png";
        if(!product.getImageProductEntityList().isEmpty()){
            url = product.getImageProductEntityList().get(0).getUrl();
        }
        Set<Map<String,Object>> listAttributeOption = new HashSet<>();
        for(ProductAttributeOptionDetail i : product.getProductAttributeOptionDetails()){
            Map<String, Object> option = new HashMap<>();
            List<Map<String,Object>> temp = new ArrayList<>();
            for(ProductAttributeOptionDetail j : product.getProductAttributeOptionDetails()){
                if(i.getAttributeOption().getIdType().getId().equals(j.getAttributeOption().getIdType().getId())){
                    Map<String,Object> data = new HashMap<>();
                    data.put("id",j.getAttributeOption().getId());
                    data.put("idType",j.getAttributeOption().getIdType().getId());
                    data.put("value",j.getAttributeOption().getValue());
                    data.put("compare",j.getValue());
                    temp.add(data);
                }
            }
            option.put("id",i.getAttributeOption().getIdType().getId());
            option.put("name",i.getAttributeOption().getIdType().getName());
            option.put("values",temp);
            listAttributeOption.add(option);
        }
        Set<Map<String,Object>> listAttributeOptionDetails = new HashSet<>();
        for(AttributeDetailEntity attributeDetail : product.getDetailEntities()){
            Map<String,Object> option =  new HashMap<>();
            option.put("idType",attributeDetail.getIdTypeDetail().getId());
            option.put("id",attributeDetail.getId());
            option.put("value",attributeDetail.getValue());
            option.put("name",attributeDetail.getIdTypeDetail().getName());
            listAttributeOptionDetails.add(option);
        }
        Double rate = productRatingService.getRateByProductId(product.getId());
        if(rate == null)rate = 0.0;
        CategoryEntity categoryTemp = product.getProductCategory();
        while (categoryTemp.getParent().getId().compareTo(UUID.fromString("00000000-0000-0000-0000-000000000000"))!=0){
            categoryTemp = categoryTemp.getParent();
        }
        return new ProductResponse(
                product.getId(),
                url,
                product.getName(),
                product.getDescription(),
                product.getProductCategory().getName(),
                rate,
                product.getPrice(),
                product.getDiscount(),
                product.getSellAmount(),
                product.getInventory(),
                product.getProductBrand().getName(),
                product.getProductBrand().getBrandCountry(),
                list,
                listAttributeOption,
                product.getCreate(),
                product.getStatus(),
                listAttributeOptionDetails,
                categoryTemp.getId());
    }
}
