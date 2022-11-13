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

import java.util.*;

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
        Page<ProductEntity> pageResult = productRepository.findByKeyword(keyword.toLowerCase(),paging);
        return pageResult.toList();
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
        List<Map<String,Object>> listAttributeOption = new ArrayList<>();
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
//
//
//        for(ProductAttributeOptionDetail productAttributeOptionDetail : product.getProductAttributeOptionDetails()){
//            Map<String, Object> attributeOption = new HashMap<>();
//            attributeOption.put("id",productAttributeOptionDetail.getAttributeOption().getId());
//            attributeOption.put("name",productAttributeOptionDetail.getAttributeOption().getValue());
//            attributeOption.put("idType",productAttributeOptionDetail.getAttributeOption().getIdType().getId());
//            attributeOption.put("nameType",productAttributeOptionDetail.getAttributeOption().getIdType().getName());
//            attributeOption.put("compareValue",productAttributeOptionDetail.getValue());
//            listAttributeOption.add(attributeOption);
//        }
        Double rate = productRatingService.getRateByProductId(product.getId());
        if(rate == null)rate = 0.0;
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
                product.getProductBrand().getName(),
                product.getProductBrand().getBrandCountry(),
                list,
                listAttributeOption,
                product.getCreate(),
                product.getStatus());
    }
}
