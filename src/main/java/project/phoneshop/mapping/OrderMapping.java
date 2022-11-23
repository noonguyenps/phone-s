package project.phoneshop.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.phoneshop.model.entity.*;
import project.phoneshop.model.payload.request.order.AddOrderRequest;
import project.phoneshop.service.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OrderMapping {
    @Autowired
    ProductService productService;
    @Autowired
    CartService cartService;

    public OrderEntity ModelToEntity(AddOrderRequest addOrderRequest, UserEntity user,List<CartEntity> cartEntities,AddressEntity address, ShipEntity ship, PaymentEntity payment, List<VoucherEntity> listVoucher){
        Double totalPrice = 0.00;
        OrderEntity order = new OrderEntity();
        order.setAddressOrder(address);
        order.setPaymentOrder(payment);
        order.setShipOrder(ship);
        order.setCreatedDate(new Date());
        order.setDelStatus(0);
        Date expDate = new Date();
        List<CartEntity> listCart = new ArrayList<>();
        expDate.setMonth(order.getCreatedDate().getMonth()+1);
        order.setExpectedDate(expDate);
        for (CartEntity cartEntity: cartEntities){
            if(cartEntity.getUserCart() == user && cartEntity.getStatus() && cartEntity.getActive()){
                totalPrice += cartEntity.getProductCart().getPrice()* cartEntity.getQuantity();
                listCart.add(cartEntity);
                order.setCartOrder(listCart);
                productService.minusProduct(cartEntity.getProductCart(),cartEntity.getQuantity());
                cartService.disActiveCart(cartEntity);
            }
        }
        totalPrice += ship.getShipPrice();
        Double voucherValue = 0.0;
        for(VoucherEntity voucher : listVoucher){
            if(voucher.getUserEntities().contains(user)){
                if(voucher.getValue().contains("%")){
                    String[] list = voucher.getValue().split("%");
                    voucherValue += totalPrice * (Double.parseDouble(list[0])/100.0);
                }
                else
                    voucherValue += Double.parseDouble(voucher.getValue());
            }
        }
        totalPrice -= voucherValue;
        order.setTotal(totalPrice);
        order.setStatusPayment(false);
        return order;
    }
    public OrderEntity ModelToEntity(AddOrderRequest addOrderRequest, UserEntity user,List<CartEntity> cartEntities,AddressEntity address, ShipEntity ship, PaymentEntity payment){
        Double totalPrice = 0.00;
        OrderEntity order = new OrderEntity();
        order.setAddressOrder(address);
        order.setPaymentOrder(payment);
        order.setShipOrder(ship);
        order.setCreatedDate(new Date());
        List<CartEntity> listCart = new ArrayList<>();
        order.setDelStatus(0);
        Date expDate = new Date();
        expDate.setMonth(order.getCreatedDate().getMonth()+1);
        order.setExpectedDate(expDate);
        for (CartEntity cartEntity: cartEntities){
            if(cartEntity.getUserCart() == user && cartEntity.getStatus() && cartEntity.getActive()){
                totalPrice += cartEntity.getProductCart().getPrice()* cartEntity.getQuantity();
                listCart.add(cartEntity);
                order.setCartOrder(listCart);
                productService.minusProduct(cartEntity.getProductCart(),cartEntity.getQuantity());
                cartService.disActiveCart(cartEntity);
            }
        }
        totalPrice += ship.getShipPrice();
        order.setTotal(totalPrice);
        order.setStatusPayment(true);
        return order;
    }
    public static OrderEntity addOrderToEntity(UserEntity user, List<CartEntity> listCart, AddressEntity address, PaymentEntity payment, ShipEntity ship, VoucherEntity voucher, Double total){
        OrderEntity order = new OrderEntity();
        order.setUserOrder(user);
        order.setName("Order");
        order.setCartOrder(listCart);
        order.setAddressOrder(address);
        order.setShipOrder(ship);
        order.setPaymentOrder(payment);
        order.setVoucherOrder(voucher);
        order.setDelStatus(0);
        LocalDate today = LocalDate.now();
        order.setCreatedDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        LocalDate expDate = today.plusDays(14);
        order.setExpectedDate(Date.from(expDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        order.setStatusPayment(false);
        if(voucher!=null){
            order.setTotal(total-Double.parseDouble(voucher.getValue())+ ship.getShipPrice());
        }
        else {
            order.setTotal(total+ ship.getShipPrice());
        }
        return order;
    }
}
