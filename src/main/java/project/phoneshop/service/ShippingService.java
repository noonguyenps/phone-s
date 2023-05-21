package project.phoneshop.service;

import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ShippingEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.response.shipping.ShippingResponse;
import project.phoneshop.model.payload.response.shipping.ShippingResponseV2;

import java.util.List;
import java.util.UUID;

public interface ShippingService {
    List<ShippingEntity> getAllShipping();

    List<ShippingEntity> getAllShippingByShipper(UserEntity user, int page, int size);

    void create(ShippingEntity shipping);
    ShippingEntity findById(UUID id);

    ShippingEntity findByShipper(UserEntity user, OrderEntity order);

    ShippingEntity getInfoShippingByOrderId(OrderEntity order);

    ShippingResponse entity2Response(ShippingEntity shipping);

    ShippingResponseV2 shippingResponse(ShippingEntity shipping);
}
