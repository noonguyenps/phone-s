package project.phoneshop.service;

import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ShippingEntity;
import project.phoneshop.model.payload.response.shipping.ShippingResponse;

import java.util.List;
import java.util.UUID;

public interface ShippingService {
    List<ShippingEntity> getAllShipping();

    void create(ShippingEntity shipping);

//    ShippingEntity getInfoShipping(UUID id);
//
    ShippingEntity getInfoShippingByOrderId(OrderEntity order);

    ShippingResponse entity2Response(ShippingEntity shipping);
}
