package project.phoneshop.service.Impl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.CartEntity;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ShippingEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.response.shipping.ShippingResponse;
import project.phoneshop.model.payload.response.shipping.ShippingResponseV2;
import project.phoneshop.repository.ShippingRepository;
import project.phoneshop.service.ShippingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public List<ShippingEntity> getAllShippingByShipper(UserEntity user, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return shippingRepository.findByUserOrderShipping(user,pageable).toList();
    }

    @Override
    public void create(ShippingEntity shipping) {
        shippingRepository.save(shipping);
    }

    @Override
    public ShippingEntity findById(UUID id) {
        Optional<ShippingEntity> shipping = shippingRepository.findById(id);
        if(!shipping.isEmpty())
            return shipping.get();
        return null;
    }

    @Override
    public ShippingEntity findByShipper(UserEntity user, OrderEntity order) {
        Optional<ShippingEntity> shipping = shippingRepository.findByUserOrderShippingAndOrderShipping(user,order);
        if(shipping.isEmpty()){
            return null;
        }else {
            return shipping.get();
        }
    }


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
        shippingResponse.setShipperPhone(shipping.getShipperPhone());
        shippingResponse.setShipperID(shipping.getShipperID());
        shippingResponse.setImage1(shipping.getImage1());
        shippingResponse.setImage2(shipping.getImage2());
        shippingResponse.setImage3(shipping.getImage3());
        shippingResponse.setState(shipping.getState());
        shippingResponse.setOrderID(shipping.getOrderShipping().getOrderId());
        List<ShippingResponse.Cart> carts =  new ArrayList<>();
        for(CartEntity cart: shipping.getOrderShipping().getCartOrder()){
            ShippingResponse.Cart cart1 = new ShippingResponse.Cart();
            cart1.setProductName(cart.getProductCart().getName());
            cart1.setProductImage(String.valueOf(cart.getProductCart().getImageProductEntityList().get(0).getUrl()));
            cart1.setQuantity(cart.getQuantity());
            carts.add(cart1);
        }
        shippingResponse.setCarts(carts);
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
    @Override
    public ShippingResponseV2 shippingResponse(ShippingEntity shipping){
        ShippingResponseV2 shippingResponse = new ShippingResponseV2();
        shippingResponse.setId(shipping.getId());
        shippingResponse.setImage1(shipping.getImage1());
        shippingResponse.setImage2(shipping.getImage2());
        shippingResponse.setImage3(shipping.getImage3());
        shippingResponse.setState(shipping.getState());
        shippingResponse.setOrderID(shipping.getOrderShipping().getOrderId());
        List<ShippingResponseV2.Cart> carts =  new ArrayList<>();
        for(CartEntity cart: shipping.getOrderShipping().getCartOrder()){
            ShippingResponseV2.Cart cart1 = new ShippingResponseV2.Cart();
            cart1.setProductName(cart.getProductCart().getName());
            cart1.setProductImage(String.valueOf(cart.getProductCart().getImageProductEntityList().get(0).getUrl()));
            cart1.setQuantity(cart.getQuantity());
            carts.add(cart1);
        }
        shippingResponse.setCarts(carts);
        shippingResponse.setOrderName(shipping.getOrderShipping().getName());
        shippingResponse.setCustomerName(shipping.getOrderShipping().getAddressOrder().getFullName());
        shippingResponse.setCustomerPhone(shipping.getOrderShipping().getAddressOrder().getPhoneNumber());
        shippingResponse.setAddressDetail(shipping.getOrderShipping().getAddressOrder().getAddressDetail());
        shippingResponse.setCommune(shipping.getOrderShipping().getAddressOrder().getCommune());
        shippingResponse.setDistrict(shipping.getOrderShipping().getAddressOrder().getDistrict());
        shippingResponse.setProvince(shipping.getOrderShipping().getAddressOrder().getProvince());
        shippingResponse.setTotal(shipping.getOrderShipping().getTotal());
        shippingResponse.setStatusPayment(shipping.getOrderShipping().getStatusPayment());
        shippingResponse.setStatusOrder(shipping.getOrderShipping().getOrderStatus());
        return shippingResponse;
    }
}
