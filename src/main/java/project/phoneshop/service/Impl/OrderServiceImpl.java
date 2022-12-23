package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.payload.response.cart.CartResponseFE;
import project.phoneshop.model.payload.response.order.OrderResponse;
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
    public List<OrderEntity> findAllOrder(int pageNo, int pageSize, String sort) {
        Pageable paging = null;
        switch (sort){
            case "total_up" : paging = PageRequest.of(pageNo, pageSize, Sort.by("total").descending());break;
            case "total_down" : paging = PageRequest.of(pageNo, pageSize, Sort.by("total").ascending());break;
            default : paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        }
        paging = PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());
        Page<OrderEntity> pagedResult = orderRepository.findAllOrder(paging);
        return pagedResult.toList();
    }
    @Override
    public OrderEntity findOrderByName(String name){
        return orderRepository.findByName(name);
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

    @Override
    public OrderResponse getOrderResponse(OrderEntity order, List<CartResponseFE> cartResponseFEs){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setCartResponseFEs(cartResponseFEs);
        orderResponse.setOrderStatus(order.getOrderStatus());
        orderResponse.setName(order.getName());
        orderResponse.setAddressOrder(order.getAddressOrder());
        orderResponse.setPaymentOrder(order.getPaymentOrder());
        orderResponse.setShipOrder(order.getShipOrder());
        orderResponse.setTotal(order.getTotal());
        orderResponse.setCreatedDate(order.getCreatedDate());
        orderResponse.setExpectedDate(order.getExpectedDate());
        orderResponse.setVoucherOrder(order.getVoucherOrder());
        return orderResponse;
    }
}
