package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.*;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface ProductRatingService {
    ProductRatingEntity saveRating(ProductRatingEntity entity);
    List<ProductRatingEntity> getAllRatingByProduct(ProductEntity product);
    List<ProductRatingEntity> getAllRatingByUser(UserEntity user);
    int countRatingLike(ProductRatingEntity entity);
    ProductRatingLikeEntity saveLike(ProductRatingLikeEntity productRatingLike);
    ProductRatingLikeEntity getLikeByRatingAndUser(ProductRatingEntity productRating,UserEntity user);
    void deleteLike(int id);
    void saveListRatingImage(List<String> urls,ProductRatingEntity ratingEntity);

    ProductRatingEntity getByUserAndProduct(UserEntity user, ProductEntity product);
    ProductRatingEntity getRatingById(int id);
    ProductRatingCommentEntity saveComment(ProductRatingCommentEntity commentEntity);
    Double getRateByProductId(UUID productId);
}
