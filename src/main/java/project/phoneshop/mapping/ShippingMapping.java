package project.phoneshop.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ShippingEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.request.shipping.AddShippingRequest;
import project.phoneshop.service.ShippingService;

import java.util.Date;

@Component
public class ShippingMapping {

    public ShippingEntity requestToEntity(OrderEntity orderEntity, UserEntity user){
        ShippingEntity shipping = new ShippingEntity();
        shipping.setOrderShipping(orderEntity);
        shipping.setUserOrderShipping(user);
        shipping.setCreate(new Date());
        shipping.setUpdate(new Date());
        shipping.setState(1);
        return shipping;
    }
}
