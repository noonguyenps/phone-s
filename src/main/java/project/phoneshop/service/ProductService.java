package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.BrandEntity;
import project.phoneshop.model.entity.CategoryEntity;
import project.phoneshop.model.entity.ProductEntity;
import project.phoneshop.model.payload.response.product.ProductResponse;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface ProductService {
    List<ProductEntity> findAllProduct();

    ProductEntity saveProduct(ProductEntity product);

    ProductEntity findById(UUID id);

    void deleteProduct(UUID id);

    void saveListImageProduct(List<String> listUrl, ProductEntity product);

    void deleteListImgProduct(ProductEntity product);

    void addAttribute(ProductEntity product, String attributeOptionId);

    void deleteAttribute(ProductEntity product, String attributeId);

    List<ProductEntity> findPaginated(int pageNo, int pageSize, String sort);

    List<ProductEntity> findProductByCategory(CategoryEntity category, int pageNo, int pageSize, String sort);

    List<ProductEntity> findProductByBrand(BrandEntity brand, int pageNo, int pageSize, String sort);

    List<ProductEntity> findProductByAttributes(CategoryEntity category, List<String> listAttribute, int pageNo, int pageSize, String sort);

    List<ProductEntity> findProductByKeyword(String keyword, int pageNo, int pageSize, String sort);

    List<ProductEntity> searchByBrand(BrandEntity brand);

    void minusProduct(ProductEntity product, int quantity);

    long countProduct();

    ProductResponse productResponse(ProductEntity product);
}
