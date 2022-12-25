package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.handler.HttpMessageNotReadableException;
import project.phoneshop.handler.MethodArgumentNotValidException;
import project.phoneshop.mapping.ProductMapping;
import project.phoneshop.mapping.ProductRatingMapping;
import project.phoneshop.model.entity.*;
import project.phoneshop.model.payload.request.product.AddProductRequest;
import project.phoneshop.model.payload.request.productRating.AddNewRatingComment;
import project.phoneshop.model.payload.request.productRating.AddNewRatingRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.model.payload.response.product.ProductResponse;
import project.phoneshop.model.payload.response.rating.RatingResponse;
import project.phoneshop.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductRatingController {
    final ProductRatingService productRatingService;
    final ImageStorageService imageStorageService;
    final ProductService productService;
    final UserService userService;
    private final CartService cartService;
    @Autowired
    AuthorizationHeader authorizationHeader;

    @GetMapping(value = "/rating/all/product")
    public ResponseEntity<SuccessResponse> getAllRatingByProduct(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "30") int size,
                                                        @RequestParam(defaultValue = "id") String sort,
                                                        @RequestParam UUID productId){
        if(!listProductRatingSort().contains(sort))
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
        List<ProductRatingEntity> list = productRatingService.findAllPageByProduct(page,size,sort,productId);
        Map<String, Object> data = new HashMap<>();
        List<RatingResponse> ratingResponses = new ArrayList<>();
        for (ProductRatingEntity productRating : list){
            ProductResponse productResponse =productService.productResponse(productRating.getProduct());
            ratingResponses.add(productRatingService.getRatingResponse(productRating,productResponse));
        }
        data.put("listRating",ratingResponses);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
    }
    @GetMapping(value = "/admin/rating/all")
    public ResponseEntity<SuccessResponse> getAllRating(HttpServletRequest request,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "30") int size,
                                                        @RequestParam(defaultValue = "id") String sort){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if(!listProductRatingSort().contains(sort))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
            List<ProductRatingEntity> list = productRatingService.findAllPage(page,size,sort);
            Map<String, Object> data = new HashMap<>();
            List<RatingResponse> ratingResponses = new ArrayList<>();
            for (ProductRatingEntity productRating : list){
                ProductResponse productResponse =productService.productResponse(productRating.getProduct());
                ratingResponses.add(productRatingService.getRatingResponse(productRating,productResponse));
            }
            data.put("listRating",ratingResponses);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping(value = "/user/rating/all")
    public ResponseEntity<SuccessResponse> getAllRatingByUser(HttpServletRequest request,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "30") int size,
                                                        @RequestParam(defaultValue = "id") String sort){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if(!listProductRatingSort().contains(sort))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
            List<ProductRatingEntity> list = productRatingService.findAllPageByUser(page,size,sort,user.getId());
            Map<String, Object> data = new HashMap<>();
            List<RatingResponse> ratingResponses = new ArrayList<>();
            for (ProductRatingEntity productRating : list){
                ProductResponse productResponse =productService.productResponse(productRating.getProduct());
                ratingResponses.add(productRatingService.getRatingResponse(productRating,productResponse));
            }
            data.put("listRating",ratingResponses);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/admin/rating/all/star")
    public ResponseEntity<SuccessResponse> getAllRatingByStar(HttpServletRequest request,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "30") int size,
                                                        @RequestParam(defaultValue = "id") String sort,
                                                        @RequestParam(defaultValue = "1") int star){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if(!listProductRatingSort().contains(sort))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
            List<ProductRatingEntity> list = productRatingService.findAllPageByRatingPoint(page,size,sort,star);
            Map<String, Object> data = new HashMap<>();
            List<RatingResponse> ratingResponses = new ArrayList<>();
            for (ProductRatingEntity productRating : list){
                ProductResponse productResponse =productService.productResponse(productRating.getProduct());
                ratingResponses.add(productRatingService.getRatingResponse(productRating,productResponse));
            }
            data.put("listRating",ratingResponses);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/admin/rating/all/product")
    public ResponseEntity<SuccessResponse> getAllRatingByProduct(HttpServletRequest request,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "30") int size,
                                                              @RequestParam(defaultValue = "id") String sort,
                                                              @RequestParam UUID productId){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if(!listProductRatingSort().contains(sort))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
            List<ProductRatingEntity> list = productRatingService.findAllPageByProduct(page,size,sort,productId);
            Map<String, Object> data = new HashMap<>();
            List<RatingResponse> ratingResponses = new ArrayList<>();
            for (ProductRatingEntity productRating : list){
                ProductResponse productResponse =productService.productResponse(productRating.getProduct());
                ratingResponses.add(productRatingService.getRatingResponse(productRating,productResponse));
            }
            data.put("listRating",ratingResponses);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/admin/rating/all/user")
    public ResponseEntity<SuccessResponse> getAllRatingByUser(HttpServletRequest request,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "30") int size,
                                                                 @RequestParam(defaultValue = "id") String sort,
                                                                 @RequestParam UUID userId){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if(!listProductRatingSort().contains(sort))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
            List<ProductRatingEntity> list = productRatingService.findAllPageByUser(page,size,sort,userId);
            Map<String, Object> data = new HashMap<>();
            List<RatingResponse> ratingResponses = new ArrayList<>();
            for (ProductRatingEntity productRating : list){
                ProductResponse productResponse =productService.productResponse(productRating.getProduct());
                ratingResponses.add(productRatingService.getRatingResponse(productRating,productResponse));
            }
            data.put("listRating",ratingResponses);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @PostMapping(value = "/user/product/ratting/uploadImg")
    public ResponseEntity<SuccessResponse> uploadImgRatingProduct(HttpServletRequest request,@RequestPart(required = true) MultipartFile file){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if(!imageStorageService.isImageFile(file))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),"The file is not an image",null), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            UUID uuid = UUID.randomUUID();
            LocalDate date = LocalDate.now();
            String url = imageStorageService.saveProductRatingImg(file, date.toString()+"/"+String.valueOf(uuid));
            if(url.equals(""))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Upload Rating Image Failure",null), HttpStatus.NOT_FOUND);
            Map<String, Object> data = new HashMap<>();
            data.put("url",url);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Upload Rating Image Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/user/product/rating/{id}")
    private ResponseEntity<SuccessResponse> insertProductRating(HttpServletRequest request,@PathVariable UUID id,@RequestBody AddNewRatingRequest addNewRatingRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            CartEntity cart = cartService.findByCartId(id);
            if(cart == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Cart not found",null),HttpStatus.NOT_FOUND);
            if(cart.getOrder().getOrderStatus()!=3||user!=cart.getUserCart())
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_ACCEPTABLE.value(), "Can't rating", null), HttpStatus.NOT_ACCEPTABLE);
            ProductRatingEntity productRating = new ProductRatingEntity();
            productRating.setProduct(cart.getProductCart());
            productRating.setRatingPoint(addNewRatingRequest.getRatingPoint());
            productRating.setDate(new Date());
            productRating.setMessage(addNewRatingRequest.getMessage());
            productRating.setUser(user);
            List<ProductRatingImageEntity> imageRating =  new ArrayList<>();
            for(String url : addNewRatingRequest.getImgUrl()){
                ProductRatingImageEntity image = new ProductRatingImageEntity();
                image.setImageLink(url);
                image.setProductRating(productRating);
                imageRating.add(image);
            }
            productRating.setImageList(imageRating);
            productRatingService.saveRating(productRating);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Save Rating Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PutMapping("/user/product/rating/{id}")
    private ResponseEntity<SuccessResponse> updateProductRating(HttpServletRequest request,@PathVariable int id,@RequestBody AddNewRatingRequest addNewRatingRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            ProductRatingEntity productRating = productRatingService.getRatingById(id);
            if(productRating == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Rating not found",null),HttpStatus.NOT_FOUND);
            productRating.setRatingPoint(addNewRatingRequest.getRatingPoint());
            productRating.setMessage(addNewRatingRequest.getMessage());
            productRating.setUser(user);
            List<ProductRatingImageEntity> imageRating =  new ArrayList<>();
            for(String url : addNewRatingRequest.getImgUrl()){
                ProductRatingImageEntity image = new ProductRatingImageEntity();
                image.setImageLink(url);
                image.setProductRating(productRating);
                imageRating.add(image);
            }
            productRating.setImageList(imageRating);
            productRatingService.saveRating(productRating);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Save Rating Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @DeleteMapping("/user/product/rating/delete/{id}")
    private ResponseEntity<SuccessResponse> deleteProductRating(HttpServletRequest request,@PathVariable int id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            ProductRatingEntity productRating = productRatingService.getRatingById(id);
            if(productRating == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Rating not found",null),HttpStatus.NOT_FOUND);
            productRatingService.deleteRating(productRating);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Delete Rating Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/admin/product/rating/delete/{id}")
    private ResponseEntity<SuccessResponse> deleteProductRatingAdmin(HttpServletRequest request,@PathVariable int id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            ProductRatingEntity productRating = productRatingService.getRatingById(id);
            if(productRating == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Rating not found",null),HttpStatus.NOT_FOUND);
            productRatingService.deleteRating(productRating);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Delete Rating Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping
    private ResponseEntity<SuccessResponse> addComment(HttpServletRequest request,@PathVariable int id, @RequestBody AddNewRatingComment addNewRatingComment){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            ProductRatingEntity productRating = productRatingService.getRatingById(id);
            if(productRating == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Rating not found",null),HttpStatus.NOT_FOUND);
            List<ProductRatingCommentEntity> productRatingCommentEntities = productRating.getCommentList();
            ProductRatingCommentEntity productRatingCommentEntity = new ProductRatingCommentEntity();
            productRatingCommentEntity.setComment(addNewRatingComment.getComment());
            productRatingCommentEntity.setUser(user);
            productRatingCommentEntity.setProductRating(productRating);
            productRatingCommentEntities.add(productRatingCommentEntity);
            productRatingService.deleteRating(productRating);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Add Rating comment Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private List<String> listProductRatingSort(){
        List<String> list = new ArrayList<>();
        list.add("id");
        list.add("date_up");
        list.add("date_down");
        list.add("rating_point_up");
        list.add("rating_point_down");
        return list;
    }}
