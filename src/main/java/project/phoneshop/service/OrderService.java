package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ProductEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.response.cart.CartResponseFE;
import project.phoneshop.model.payload.response.order.OrderResponse;

import java.util.List;

@Component
@Service
public interface OrderService {
    OrderEntity findById(int id);

    List<OrderEntity> findAllOrder(int pageNo, int pageSize, String sort);

    List<OrderEntity> findAllOrderByStatus(int status, int pageNo, int pageSize);

    List<OrderEntity> findAllOrderByUser(int status, int pageNo, int pageSize, UserEntity user);

    OrderEntity findOrderByName(String name);

    List<OrderEntity> getAll();
    OrderEntity save(OrderEntity order);
    void delete(int id);
    void changePaymentStatus(int id,boolean status);
    long countOrder();
    double countOrderPrice();
    double countPayMoney(OrderEntity order);
    List<Object> countUserPerMonth();

    OrderResponse getOrderResponse(OrderEntity order, List<CartResponseFE> cartResponseFEs);

    OrderResponse getOrderAdminResponse(OrderEntity order, List<CartResponseFE> cartResponseFEs);

    OrderEntity findShipping(int orderId, String secretKey);
}
