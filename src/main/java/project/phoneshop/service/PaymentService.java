package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.PaymentEntity;

import java.util.List;

@Component
@Service
public interface PaymentService {
    List<PaymentEntity> getAll();
    PaymentEntity getPaymentById(int payment_id);
    PaymentEntity create(PaymentEntity payment);
    PaymentEntity update(PaymentEntity payment);
    void delete(int payment_id);
}
