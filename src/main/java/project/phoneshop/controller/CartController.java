package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.mapping.CartMapping;
import project.phoneshop.model.entity.CartEntity;
import project.phoneshop.model.entity.ProductEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.request.cart.AddNewCartRequest;
import project.phoneshop.model.payload.request.cart.UpdateCartRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.service.CartService;
import project.phoneshop.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    @Autowired
    AuthorizationHeader authorizationHeader;
    @GetMapping("/user/cart")
    public ResponseEntity<SuccessResponse> getAllCartByUser(HttpServletRequest request){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<CartEntity> listCart = user.getListCart();
            Map<String,Object> data = new HashMap<>();
            data.put("listCart",listCart);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"List Cart",data),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/user/cart/insert")
    public ResponseEntity<SuccessResponse> insertToCart(HttpServletRequest request, AddNewCartRequest addNewCartRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            ProductEntity product = productService.findById(addNewCartRequest.getProductId());
            if(product == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Product not found",null),HttpStatus.NOT_FOUND);
            for(CartEntity cartEntity: user.getListCart()){
                if(cartEntity.getProductCart() == product){
                    CartEntity cart = cartService.getCartByProduct(user,product);
                    cartEntity.setQuantity(cartEntity.getQuantity()+addNewCartRequest.getQuantity());
                    cartService.saveCart(cart);
                    return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Add product to Cart successfully",null),HttpStatus.OK);
                }
            }
            CartEntity cartEntity = CartMapping.getCartByRequest(addNewCartRequest,user,product);
            cartService.saveCart(cartEntity);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Add product to Cart successfully",null),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PutMapping("/user/cart/update/{id}")
    public ResponseEntity<SuccessResponse> updateToCart(@PathVariable UUID id,HttpServletRequest request, UpdateCartRequest updateCartRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            CartEntity cartEntity = cartService.findByCartId(id);
            if(cartEntity == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Cart not found",null),HttpStatus.NOT_FOUND);
            cartEntity.setQuantity(updateCartRequest.getQuantity());
            cartEntity.setStatus(updateCartRequest.getStatus());
            cartService.saveCart(cartEntity);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Update product to Cart successfully",null),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @DeleteMapping("/user/cart/delete/{id}")
    public ResponseEntity<SuccessResponse> deleteToCart(HttpServletRequest request, @PathVariable UUID id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            try {
                cartService.deleteCartItem(id);
                return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Delete product to Cart successfully",null),HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_ACCEPTABLE.value(),"Delete product to Cart failure",null),HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
