package project.phoneshop.repository;

import org.hibernate.annotations.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.phoneshop.model.entity.CategoryEntity;
import project.phoneshop.model.entity.ProductEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@EnableJpaRepositories
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    @Query(value = "SELECT * FROM products",
            countQuery = "SELECT count(*) FROM products",
            nativeQuery = true)
    Page<ProductEntity> findAllProduct(Pageable pageable);
    List<ProductEntity> findByProductCategory(CategoryEntity category);
    @Query(value = "SELECT * FROM products WHERE category_id in ?1 and products.product_status=1",
            countQuery = "SELECT count(*) FROM products WHERE category_id in ?1 and products.product_status=1",
            nativeQuery = true)
    Page<ProductEntity> findByCategory(List<UUID> categoryIds, Pageable pageable);
    @Query(value = "SELECT * FROM products WHERE brand_id = ?1 and products.product_status=1",
            countQuery = "SELECT count(*) FROM products WHERE brand_id = ?1 and products.product_status=1",
            nativeQuery = true)
    Page<ProductEntity> findByBrand(UUID brandId, Pageable pageable);
    @Query(value = "SELECT A.* FROM (SELECT * FROM products WHERE category_id in ?1) as A, (SELECT DISTINCT product_id FROM product_attribute_options WHERE id in ?2) as B WHERE A.product_id = B.product_id",
            countQuery = "SELECT A.* FROM (SELECT * FROM products WHERE category_id in ?1) as A, (SELECT DISTINCT product_id FROM product_attribute_options WHERE id in ?2) as B WHERE A.product_id = B.product_id",
            nativeQuery = true)
    Page<ProductEntity> findByAttributes(List<UUID> categoryIds, List<String>attribute, Pageable pageable);
    @Query(value = "SELECT * FROM products WHERE converttvkdau(LOWER(products.product_name)) LIKE %?1% OR converttvkdau(LOWER(products.product_name)) LIKE %?2% AND products.product_status=1",
            countQuery = "SELECT * FROM products WHERE converttvkdau(LOWER(products.product_name)) LIKE %?1%  OR converttvkdau(LOWER(products.product_name)) LIKE %?2%  AND products.product_status=1",
            nativeQuery = true)
    Page<ProductEntity> findByKeyword(String keyword1, String keyword2, Pageable pageable);
    @Query(value = "SELECT * FROM products WHERE products.product_status=?1",
            countQuery = "SELECT count(*) FROM products WHERE products.product_status=?1",
            nativeQuery = true)
    Page<ProductEntity> findAllProductByStatus(int status, Pageable pageable);
    @Query(value = "select category_name, total\n" +
            "from category, (select b.order_id ,b.total,\"get_category_parent_root\"(products.category_id) from (select a.order_id ,a.total,carts.cart_id,carts.product_id from (select * from public.orders where status=2) as a, public.carts where a.order_id = carts.order_id) as b, products where products.product_id = b.product_id) as c\n" +
            "where get_category_parent_root=category.category_id",
            nativeQuery = true)
    List<Object> sumTotalPerCategory();
}
