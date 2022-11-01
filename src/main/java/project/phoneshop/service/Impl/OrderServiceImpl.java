package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.repository.OrderRepository;
import project.phoneshop.service.OrderService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    final OrderRepository orderRepository;

    @Override
    public OrderEntity findById(int id) {
        Optional<OrderEntity> orderEntity = orderRepository.findByOrderId(id);
        if(orderEntity==null){
            return null;
        }
        return orderEntity.get();
    }

    @Override
    public List<OrderEntity> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public OrderEntity save(OrderEntity order) {
        return orderRepository.save(order);
    }
    @Override
    public long countOrder() {
        return orderRepository.count();
    }

    @Override
    public long countOrderPrice() {
        return orderRepository.countPrice();
    }

    @Override
    public double countPayMoney(OrderEntity order) {return order.getTotal();}

    @Override
    public List<Object> countUserPerMonth() {
        return orderRepository.countOrderPerMonth();
    }

    @Override
    public void delete(int id) {
        orderRepository.deleteById(id);
    }
}
