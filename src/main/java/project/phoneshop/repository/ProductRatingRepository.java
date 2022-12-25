package project.phoneshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT * FROM product_ratings",
            countQuery = "SELECT count(*) FROM product_ratings",
            nativeQuery = true)
    Page<ProductRatingEntity> findAllRating(Pageable pageable);

    @Query(value = "SELECT * FROM product_ratings WHERE rating_point=?1",
            countQuery = "SELECT count(*) FROM product_ratings WHERE rating_point=?1",
            nativeQuery = true)
    Page<ProductRatingEntity> findAllRatingByRatingPoint(int point,Pageable pageable);

    @Query(value = "SELECT * FROM product_ratings WHERE product_id=?1",
            countQuery = "SELECT count(*) FROM product_ratings WHERE product_id=?1",
            nativeQuery = true)
    Page<ProductRatingEntity> findAllRatingByProduct(UUID productId,Pageable pageable);

    @Query(value = "SELECT * FROM product_ratings WHERE user_id=?1",
            countQuery = "SELECT count(*) FROM product_ratings WHERE user_id=?1",
            nativeQuery = true)
    Page<ProductRatingEntity> findAllRatingByUser(UUID userId,Pageable pageable);
}
