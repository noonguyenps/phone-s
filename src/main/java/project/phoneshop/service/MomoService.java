package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.OrderEntity;

@Component
@Service
public interface MomoService {
    String createMomoPayment(OrderEntity orderEntity) throws Exception;

    String createMomoATMPayment(OrderEntity orderEntity) throws Exception;

    int getResultCode(int orderId, String requestId) throws Exception;
}
