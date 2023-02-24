package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.phoneshop.model.entity.*;
import project.phoneshop.model.payload.response.product.ProductResponse;
import project.phoneshop.model.payload.response.rating.RatingResponse;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface ProductRatingService {
    ProductRatingEntity saveRating(ProductRatingEntity entity);

    void  deleteRating(ProductRatingEntity productRating);

    List<ProductRatingEntity> findAllPage(int page, int size, String sort);

    List<ProductRatingEntity> findAllPageByRatingPoint(int page, int size, String sort, int point);

    List<ProductRatingEntity> findAllPageByUser(int page, int size, String sort, UUID userId);

    List<ProductRatingEntity> findAllPageByProduct(int page, int size, String sort, UUID productId);

    RatingResponse getRatingResponse(ProductRatingEntity productRating, ProductResponse productResponse);

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
