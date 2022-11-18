package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.CartEntity;
import project.phoneshop.model.entity.ProductAttributeOptionDetail;
import project.phoneshop.model.entity.ProductEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.response.cart.CartResponse;
import project.phoneshop.model.payload.response.cart.CartResponseFE;
import project.phoneshop.repository.CartRepository;
import project.phoneshop.service.CartService;
import project.phoneshop.service.ProductService;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    final CartRepository cartRepository;
    final ProductService productService;
    @Override
    public CartEntity saveCart(CartEntity cart) {
        return cartRepository.save(cart);
    }
    @Override
    public List<CartEntity> getCartByUid(UserEntity user) {
        List<CartEntity> cart = cartRepository.findByUserCart(user);
        if (cart.isEmpty())
            return null;
        return cart;
    }

    @Override
    public List<CartEntity> getCartByProduct(UserEntity user,ProductEntity product) {
        List<CartEntity> cartEntity = cartRepository.findByUserCartAndProductCart(user,product);
        return cartEntity;
    }
    @Override
    public CartResponse getCartResponse(CartEntity cart){
        List<Map<String,Object>> list = new ArrayList<>();
        for(String id : cart.getListAttributeOption()){
            for(ProductAttributeOptionDetail productAttributeOptionDetail: cart.getProductCart().getProductAttributeOptionDetails()){
                if(productAttributeOptionDetail.getAttributeOption().getId().equals(id)){
                    Map<String,Object> data = new HashMap<>();
                    data.put("idAttributeOption",productAttributeOptionDetail.getAttributeOption().getId());
                    data.put("attributeOptionName",productAttributeOptionDetail.getAttributeOption().getValue());
                    data.put("attributeOptionType",productAttributeOptionDetail.getAttributeOption().getIdType().getName());
                    data.put("attributeOptionCompare",productAttributeOptionDetail.getValue());
                    list.add(data);
                }
            }
        }
        CartResponse cartResponse = new CartResponse(cart.getId(),
                cart.getProductCart().getId(),
                cart.getProductCart().getName(),
                cart.getProductCart().getImageProductEntityList().get(0).getUrl(),
                cart.getProductCart().getPrice(),
                cart.getQuantity(),
                cart.getStatus(),
                cart.getActive(),
                list);
        return cartResponse;
    }
    @Override
    public CartResponseFE getCartResponseFE(CartEntity cart){
        Double price = cart.getProductCart().getPrice();
        List<String> option = new ArrayList<>();
        for(ProductAttributeOptionDetail productAttributeOptionDetail: cart.getProductCart().getProductAttributeOptionDetails()){
            if(cart.getListAttributeOption().contains(productAttributeOptionDetail.getAttributeOption().getId())){
                price= price+ productAttributeOptionDetail.getValue();
                option.add(productAttributeOptionDetail.getAttributeOption().getIdType().getName()+ " : "+productAttributeOptionDetail.getAttributeOption().getValue());
            }
        }
        String url = "https://res.cloudinary.com/duk2lo18t/image/upload/v1667887284/frontend/R_zzr2lm.png";
        if(!cart.getProductCart().getImageProductEntityList().isEmpty()){
            url = cart.getProductCart().getImageProductEntityList().get(0).getUrl();
        }
        CartResponseFE cartResponseFE = new CartResponseFE(cart.getStatus(),
                option,
                cart.getListAttributeOption(),
                cart.getId(),
                cart.getProductCart().getName(),
                url,
                cart.getProductCart().getId(),
                Double.valueOf(Math.round(price*(1-cart.getProductCart().getDiscount()/100))),
                cart.getQuantity()
                );
        return cartResponseFE;
    }
//
//    @Override
//    public List<CartItemEntity> getCartItem(CartEntity cart) {
//        return null;
//    }
//
//    @Override
//    public void deleteCartById(UUID id) {
//
//    }
//    @Override
//    public int calCartTotal(CartEntity cart) {
//        return 0;
//    }
//
//    @Override
//    public CartItemEntity saveCartItem(CartItemEntity cartItem) {
//        return cartItemRepository.save(cartItem);
//    }
//
//    @Override
//    public CartItemEntity getCartItemByPidAndCid(ProductEntity id,CartEntity cart) {
//
//        Optional<CartItemEntity> cartItem = cartItemRepository.findByProductAndCart(id,cart);
//        if (cartItem.isEmpty())
//            return null;
//        return cartItem.get();
//    }
//
    @Override
    public void deleteCartItem(UUID id) {
        cartRepository.deleteCartById(id);
    }

    @Override
    public CartEntity findByCartId(UUID cartId) {
        Optional<CartEntity> cartEntity = cartRepository.findById(cartId);
        return cartEntity.get();
    }
    @Override
    public void disActiveCart(CartEntity cartEntity){
        cartEntity.setActive(false);
        cartRepository.save(cartEntity);
    }

    @Override
    public void deleteAllCart(UserEntity user) {
        cartRepository.deleteCartByUser(user);
    }
//
//    @Override
//    public CartItemEntity getCartItemById(int id) {
//        Optional<CartItemEntity> cartItem = cartItemRepository.findById(id);
//        if (cartItem.isEmpty())
//            return null;
//        return cartItem.get();
//    }
//
//    @Override
//    public CartEntity getCartByUid(UserEntity user, Boolean status) {
//        Optional<CartEntity> cart = cartRepository.findByUserAndStatus(user,status);
//        if (cart.isEmpty())
//            return null;
//        return cart.get();
//    }
//
//
//    @Override
//    public CartItemResponse cartItemResponse(CartItemEntity cartItem){
//        return new CartItemResponse(
//                cartItem.getId(),
//                productService.productResponse(cartItem.getProduct()),
//                cartItem.getCart().getId(),
//                cartItem.getQuantity());
//    }
//
//    @Override
//    public CartItemEntity getItemByIdAndCart(int id, CartEntity cart) {
//        Optional<CartItemEntity> cartItem = cartItemRepository.findByIdAndCart(id,cart);
//        if(cartItem.isEmpty())
//            return null;
//        return cartItem.get();
//    }
}
