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
import project.phoneshop.model.payload.response.rating.RatingResponse;
import project.phoneshop.repository.ProductRatingCommentRepository;
import project.phoneshop.repository.ProductRatingImageRepository;
import project.phoneshop.repository.ProductRatingLikeRepository;
import project.phoneshop.repository.ProductRatingRepository;
import project.phoneshop.service.ProductRatingService;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductRatingServiceImpl implements ProductRatingService {
    final ProductRatingRepository productRatingRepository;
    final ProductRatingLikeRepository productRatingLikeRepository;
    final ProductRatingImageRepository productRatingImageRepository;
    final ProductRatingCommentRepository productRatingCommentRepository;
    @Override
    public ProductRatingEntity saveRating(ProductRatingEntity entity) {
        return productRatingRepository.save(entity);
    }
    @Override
    public void  deleteRating(ProductRatingEntity productRating){
        productRatingRepository.delete(productRating);
    }

    @Override
    public List<ProductRatingEntity> findAllPage(int page, int size, String sort) {
        Pageable paging = null;
        switch (sort){
            case "date_up" : paging = PageRequest.of(page, size, Sort.by("date").descending());break;
            case "date_down" : paging = PageRequest.of(page, size, Sort.by("date").ascending());break;
            case "rating_point_up" : paging = PageRequest.of(page, size, Sort.by("rating_point").descending());break;
            case "rating_point_down" : paging = PageRequest.of(page, size, Sort.by("rating_point").ascending());break;
            default : paging = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<ProductRatingEntity> pagedResult = productRatingRepository.findAllRating(paging);
        return pagedResult.toList();
    }

    @Override
    public List<ProductRatingEntity> findAllPageByRatingPoint(int page, int size, String sort, int point) {
        Pageable paging = null;
        switch (sort){
            case "date_up" : paging = PageRequest.of(page, size, Sort.by("date").descending());break;
            case "date_down" : paging = PageRequest.of(page, size, Sort.by("date").ascending());break;
            case "rating_point_up" : paging = PageRequest.of(page, size, Sort.by("rating_point").descending());break;
            case "rating_point_down" : paging = PageRequest.of(page, size, Sort.by("rating_point").ascending());break;
            default : paging = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<ProductRatingEntity> pagedResult = productRatingRepository.findAllRatingByRatingPoint(point, paging);
        return pagedResult.toList();
    }

    @Override
    public List<ProductRatingEntity> findAllPageByUser(int page, int size, String sort, UUID userId) {
        Pageable paging = null;
        switch (sort){
            case "date_up" : paging = PageRequest.of(page, size, Sort.by("date").descending());break;
            case "date_down" : paging = PageRequest.of(page, size, Sort.by("date").ascending());break;
            case "rating_point_up" : paging = PageRequest.of(page, size, Sort.by("rating_point").descending());break;
            case "rating_point_down" : paging = PageRequest.of(page, size, Sort.by("rating_point").ascending());break;
            default : paging = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<ProductRatingEntity> pagedResult = productRatingRepository.findAllRatingByUser(userId, paging);
        return pagedResult.toList();
    }

    @Override
    public List<ProductRatingEntity> findAllPageByProduct(int page, int size, String sort, UUID productId) {
        Pageable paging = null;
        switch (sort){
            case "date_up" : paging = PageRequest.of(page, size, Sort.by("date").descending());break;
            case "date_down" : paging = PageRequest.of(page, size, Sort.by("date").ascending());break;
            case "rating_point_up" : paging = PageRequest.of(page, size, Sort.by("rating_point").descending());break;
            case "rating_point_down" : paging = PageRequest.of(page, size, Sort.by("rating_point").ascending());break;
            default : paging = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<ProductRatingEntity> pagedResult = productRatingRepository.findAllRatingByProduct(productId, paging);
        return pagedResult.toList();
    }
    @Override
    public RatingResponse getRatingResponse(ProductRatingEntity productRating, ProductResponse productResponse){
        RatingResponse response = new RatingResponse();
        response.setId(productRating.getId());
        response.setStar(productRating.getRatingPoint());
        response.setComment(productRating.getMessage());
        response.setNickname(productRating.getUser().getNickName());
        response.setProductResponse(productResponse);
        List<Map<String,Object>> comments = new ArrayList<>();
        for(ProductRatingCommentEntity productRatingCommentEntity : productRating.getCommentList()){
            Map<String,Object> comment = new HashMap<>();
            if(productRatingCommentEntity.getUser().getNickName()==null){
                comment.put("userNickName","Người dùng S-Phone");
            }
            else comment.put("userNickName",productRatingCommentEntity.getUser().getNickName());
            comment.put("comment",productRatingCommentEntity.getComment());
            comments.add(comment);
        }
        response.setComments(comments);
        List<String> urls = new ArrayList<>();
        for(ProductRatingImageEntity productRatingImageEntity : productRating.getImageList()){
            urls.add(productRatingImageEntity.getImageLink());
        }
        response.setUrls(urls);
        return response;
    }

    @Override
    public List<ProductRatingEntity> getAllRatingByProduct(ProductEntity product) {
        List<ProductRatingEntity> list = productRatingRepository.getAllByProduct(product);
        return list;
    }

    @Override
    public List<ProductRatingEntity> getAllRatingByUser(UserEntity user) {
        List<ProductRatingEntity> list = productRatingRepository.getAllByUser(user);
        return list;
    }

    @Override
    public int countRatingLike(ProductRatingEntity entity) {
        List<ProductRatingLikeEntity> list = productRatingLikeRepository.findAllByProductRating(entity);
        return list.size();


    }

    @Override
    public ProductRatingLikeEntity saveLike(ProductRatingLikeEntity productRatingLike) {
        return productRatingLikeRepository.save(productRatingLike);
    }

    @Override
    public ProductRatingLikeEntity getLikeByRatingAndUser(ProductRatingEntity productRating,UserEntity user) {
        Optional<ProductRatingLikeEntity> likeEntity = productRatingLikeRepository.findByProductRatingAndUser(productRating,user);
        if (likeEntity.isEmpty())
            return null;
        return likeEntity.get();

    }

    @Override
    public void deleteLike(int id) {
        productRatingLikeRepository.deleteById(id);
    }

    @Override
    public void saveListRatingImage(List<String> urls, ProductRatingEntity ratingEntity) {

        for (String url : urls){
            ProductRatingImageEntity entity = new ProductRatingImageEntity();
            entity.setProductRating(ratingEntity);
            entity.setImageLink(url);
            productRatingImageRepository.save(entity);
        }


    }

    @Override
    public ProductRatingEntity getByUserAndProduct(UserEntity user, ProductEntity product) {
        Optional<ProductRatingEntity> productRating = productRatingRepository.getByUserAndProduct(user,product);
        if(productRating.isEmpty())
            return null;
        return productRating.get();
    }

    @Override
    public ProductRatingEntity getRatingById(int id) {
        Optional<ProductRatingEntity> productRating = productRatingRepository.findById(id);
        if(productRating.isEmpty())
            return null;
        return productRating.get();
    }

    @Override
    public ProductRatingCommentEntity saveComment(ProductRatingCommentEntity commentEntity) {
        return productRatingCommentRepository.save(commentEntity);
    }
    @Override
    public Double getRateByProductId(UUID productId) {
        return productRatingRepository.getRatingPointByProductId(productId);
    }
}
