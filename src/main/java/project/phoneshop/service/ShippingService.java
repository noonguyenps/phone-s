package project.phoneshop.service;

import project.phoneshop.model.entity.ShippingEntity;

import java.util.List;
import java.util.UUID;

public interface ShippingService {
    List<ShippingEntity> getAllShipping();

//    ShippingEntity getInfoShipping(UUID id);
//
//    ShippingEntity getInfoShippingByOrderId(int id);
}
