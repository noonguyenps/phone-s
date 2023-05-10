package project.phoneshop.service.Impl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.CartEntity;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ShippingEntity;
import project.phoneshop.model.payload.response.shipping.ShippingResponse;
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

    @Override
    public ShippingResponse entity2Response(ShippingEntity shipping){
        ShippingResponse shippingResponse = new ShippingResponse();
        shippingResponse.setId(shipping.getId());
        shippingResponse.setShipperName(shipping.getShipperName());
        shippingResponse.setShipperPhone(shippingResponse.getShipperPhone());
        shippingResponse.setImage1(shippingResponse.getImage1());
        shippingResponse.setImage2(shippingResponse.getImage2());
        shippingResponse.setImage3(shippingResponse.getImage3());
        shippingResponse.setState(shipping.getState());
        shippingResponse.setOrderID(shipping.getOrderShipping().getOrderId());
        for(CartEntity cart: shipping.getOrderShipping().getCartOrder()){
            ShippingResponse.Cart cart1 = new ShippingResponse.Cart();
            cart1.setProductName(cart.getProductCart().getName());
            cart1.setProductImage(String.valueOf(cart.getProductCart().getImageProductEntityList().get(0)));
            cart1.setQuantity(cart.getQuantity());
        }
        shippingResponse.setOrderName(shipping.getOrderShipping().getName());
        shippingResponse.setCustomerName(shipping.getOrderShipping().getAddressOrder().getFullName());
        shippingResponse.setCustomerPhone(shipping.getOrderShipping().getAddressOrder().getPhoneNumber());
        shippingResponse.setAddressDetail(shipping.getOrderShipping().getAddressOrder().getAddressDetail());
        shippingResponse.setCommune(shipping.getOrderShipping().getAddressOrder().getCommune());
        shippingResponse.setDistrict(shipping.getOrderShipping().getAddressOrder().getDistrict());
        shippingResponse.setProvince(shipping.getOrderShipping().getAddressOrder().getProvince());
        shippingResponse.setTotal(shipping.getOrderShipping().getTotal());
        shippingResponse.setStatusPayment(shipping.getOrderShipping().getStatusPayment());
        return shippingResponse;
    }
}
