package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.CartEntity;
import project.phoneshop.model.entity.ProductEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.response.cart.CartResponse;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface CartService {
    CartEntity saveCart(CartEntity cart);
    List<CartEntity> getCartByUid(UserEntity uid);
    CartEntity getCartByProduct(UserEntity user,ProductEntity product);

    CartResponse getCartResponse(CartEntity cart);

    void deleteCartItem(UUID id);
    CartEntity findByCartId(UUID cartId);

    void disActiveCart(CartEntity cartEntity);
//
//    List<CartItemEntity> getCartItem(CartEntity cart);

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
    //
//
//    int calCartTotal(CartEntity cart);
//
//    CartItemEntity saveCartItem(CartItemEntity cartItem);
//
//    CartItemEntity getCartItemByPidAndCid(ProductEntity id, CartEntity cart);
//
//    void deleteCartItem(int id);
//
//    CartItemEntity getCartItemById(int id);
//
//    CartEntity getCartByUid(UserEntity uid, Boolean status);
//
//
//    CartItemResponse cartItemResponse(CartItemEntity cartItem);
//
//    public CartItemEntity getItemByIdAndCart(int id, CartEntity cart);
}