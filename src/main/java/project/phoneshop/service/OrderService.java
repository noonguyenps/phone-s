package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.OrderEntity;

import java.util.List;

@Component
@Service
public interface OrderService {
    OrderEntity findById(int id);
    List<OrderEntity> getAll();
    OrderEntity save(OrderEntity order);
    void delete(int id);

    long countOrder();
    long countOrderPrice();
    double countPayMoney(OrderEntity order);
    List<Object> countUserPerMonth();
}
