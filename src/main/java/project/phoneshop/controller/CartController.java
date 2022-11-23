package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.mapping.CartMapping;
import project.phoneshop.model.entity.*;
import project.phoneshop.model.payload.request.cart.AddNewCartRequest;
import project.phoneshop.model.payload.request.cart.UpdateCartRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.model.payload.response.cart.CartResponse;
import project.phoneshop.model.payload.response.cart.CartResponseFE;
import project.phoneshop.service.AttributeService;
import project.phoneshop.service.CartService;
import project.phoneshop.service.ProductService;
import project.phoneshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final AttributeService attributeService;
    private final UserService userService;
    @Autowired
    AuthorizationHeader authorizationHeader;
    @GetMapping("/user/cart")
    public ResponseEntity<SuccessResponse> getAllCartByUser(HttpServletRequest request){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<CartEntity> listCart = user.getListCart();
            Map<String,Object> data = new HashMap<>();
            List<CartResponseFE> cartResponseList = new ArrayList<>();
            for(CartEntity cart : listCart){
                if(cart.getActive())
                    cartResponseList.add(cartService.getCartResponseFE(cart));
            }
            data.put("listCart",cartResponseList);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"List Cart",data),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/user/cart/insert")
    public ResponseEntity<SuccessResponse> insertToCart(HttpServletRequest request,@RequestBody AddNewCartRequest addNewCartRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            ProductEntity product = productService.findById(addNewCartRequest.getProductId());
            if(product == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Product not found",null),HttpStatus.NOT_FOUND);
            List<String> listAttributeType = new ArrayList<>();
            for(ProductAttributeOptionDetail productAttributeOptionDetail: product.getProductAttributeOptionDetails()){
                if(!listAttributeType.contains(productAttributeOptionDetail.getAttributeOption().getIdType().getId())){
                    listAttributeType.add(productAttributeOptionDetail.getAttributeOption().getIdType().getId());
                }
            }
            List<String> listAttribute = new ArrayList<>();
            for(String attributeOptionId : addNewCartRequest.getListAttribute()){
                AttributeOptionEntity attributeOption = attributeService.findByIdAttributeOption(attributeOptionId);
                if(attributeOption == null)
                    return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.CONFLICT.value(), "Attribute option id :"+attributeOptionId+ " Not found",null),HttpStatus.CONFLICT);
                if(listAttributeType.contains(attributeOption.getIdType().getId()))
                    listAttributeType.remove(attributeOption.getIdType().getId());
                if(listAttribute.contains(attributeOption.getIdType().getId()))
                    return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.CONFLICT.value(), "Conflict attribute option",null),HttpStatus.CONFLICT);
                else
                    listAttribute.add(attributeOption.getIdType().getId());
            }
            if(!listAttributeType.isEmpty()){
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_FOUND.value(), "List attribute option not found",null),HttpStatus.NOT_FOUND);
            }
            for(CartEntity cartEntity: user.getListCart()){
                if(cartEntity.getProductCart() == product && cartEntity.getListAttributeOption().equals(addNewCartRequest.getListAttribute())){
                    List<CartEntity> listCart = cartService.getCartByProduct(user,product);
                    for(CartEntity cart: listCart){
                        cartEntity.setQuantity(cartEntity.getQuantity()+addNewCartRequest.getQuantity());
                        cartService.saveCart(cart);
                        return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Add product to Cart successfully",null),HttpStatus.OK);
                    }
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
    public ResponseEntity<SuccessResponse> updateToCart(@PathVariable UUID id,HttpServletRequest request,@RequestBody UpdateCartRequest updateCartRequest){
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
    @DeleteMapping("/user/cart/delete/all")
    private ResponseEntity deleteAllCart(HttpServletRequest request){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            try {
                cartService.deleteAllCart(user);
                return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Delete All product to Cart successfully",null),HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_ACCEPTABLE.value(),"Delete All product to Cart failure",null),HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
    @PutMapping("/user/cart/choose/all")
    private ResponseEntity chooseAllCart(HttpServletRequest request,@RequestParam boolean status){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            try {
                for(CartEntity cart : user.getListCart()){
                    cart.setStatus(status);
                }
                userService.saveInfo(user);
                return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Set All status Cart successfully",null),HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_ACCEPTABLE.value(),"Set All status Cart failure",null),HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
