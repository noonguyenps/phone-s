package project.phoneshop.service.Impl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ShippingEntity;
import project.phoneshop.repository.ShippingRepository;
import project.phoneshop.service.ShippingService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {
    private final ShippingRepository shippingRepository;

    @Override
    public List<ShippingEntity> getAllShipping(){
        List<ShippingEntity> shippingEntities = shippingRepository.findAll();
        return shippingEntities;
    }

    @Override
    public void create(ShippingEntity shipping) {
        shippingRepository.save(shipping);
    }

//    @Override
//    public ShippingEntity getInfoShipping(UUID id){
//        Optional<ShippingEntity> shipping = shippingRepository.findById(id);
//        if(shipping.isEmpty())
//            return null;
//        else
//            return shipping.get();
//    }

    @Override
    public ShippingEntity getInfoShippingByOrderId(OrderEntity order){
        Optional<ShippingEntity> shipping = shippingRepository.findByOrderShipping(order);
        if(shipping.isEmpty())
            return null;
        else
            return shipping.get();
    }
}
