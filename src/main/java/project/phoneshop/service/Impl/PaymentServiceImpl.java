package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.PaymentEntity;
import project.phoneshop.repository.PaymentRepository;
import project.phoneshop.service.PaymentService;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    final PaymentRepository paymentRepository;


    @Override
    public List<PaymentEntity> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentEntity getPaymentById(int payment_id) {
        Optional<PaymentEntity> payment = paymentRepository.findByPaymentId(payment_id);
        if(payment.isEmpty()){
            return null;
        }
        return payment.get();
    }

    @Override
    public PaymentEntity create(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public PaymentEntity update(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void delete(int payment_id) {
        paymentRepository.deleteById(payment_id);
    }
}
