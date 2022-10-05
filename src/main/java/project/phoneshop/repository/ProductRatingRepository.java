package project.phoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.phoneshop.model.entity.ProductEntity;
import project.phoneshop.model.entity.ProductRatingEntity;
import project.phoneshop.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRatingRepository extends JpaRepository<ProductRatingEntity,Integer> {
    List<ProductRatingEntity> getAllByProduct(ProductEntity product);
    List<ProductRatingEntity> getAllByUser(UserEntity user);
    Optional<ProductRatingEntity> getByUserAndProduct(UserEntity user, ProductEntity product);
    @Query(value = "select AVG(rating_point) from product_ratings where product_id= ?1",
            countQuery = "select AVG(rating_point) from product_ratings where product_id= ?1",
            nativeQuery = true)
    Double getRatingPointByProductId(UUID productId);
}
