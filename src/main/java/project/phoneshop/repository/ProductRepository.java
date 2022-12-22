package project.phoneshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.phoneshop.model.entity.CategoryEntity;
import project.phoneshop.model.entity.ProductEntity;

import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    @Query(value = "SELECT * FROM products",
            countQuery = "SELECT count(*) FROM products",
            nativeQuery = true)
    Page<ProductEntity> findAllProduct(Pageable pageable);
    List<ProductEntity> findByProductCategory(CategoryEntity category);
    @Query(value = "SELECT * FROM products WHERE category_id in ?1",
            countQuery = "SELECT count(*) FROM products WHERE category_id in ?1",
            nativeQuery = true)
    Page<ProductEntity> findByCategory(List<UUID> categoryIds, Pageable pageable);
    @Query(value = "SELECT * FROM products WHERE brand_id = ?1",
            countQuery = "SELECT count(*) FROM products WHERE brand_id = ?1",
            nativeQuery = true)
    Page<ProductEntity> findByBrand(UUID brandId, Pageable pageable);
    @Query(value = "SELECT A.* FROM (SELECT * FROM products WHERE category_id in ?1) as A, (SELECT DISTINCT product_id FROM product_attribute_options WHERE id in ?2) as B WHERE A.product_id = B.product_id",
            countQuery = "SELECT A.* FROM (SELECT * FROM products WHERE category_id in ?1) as A, (SELECT DISTINCT product_id FROM product_attribute_options WHERE id in ?2) as B WHERE A.product_id = B.product_id",
            nativeQuery = true)
    Page<ProductEntity> findByAttributes(List<UUID> categoryIds, List<String>attribute, Pageable pageable);
    @Query(value = "SELECT * FROM products WHERE LOWER(products.product_name) LIKE %?1%",
            countQuery = "SELECT * FROM products WHERE LOWER(products.product_name) LIKE %?1%",
            nativeQuery = true)
    Page<ProductEntity> findByKeyword(String keyword, Pageable pageable);
    @Query(value = "SELECT * FROM products WHERE products.product_status=?1",
            countQuery = "SELECT count(*) FROM products WHERE products.product_status=?1",
            nativeQuery = true)
    Page<ProductEntity> findAllProductByStatus(int status, Pageable pageable);
}
