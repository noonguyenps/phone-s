package project.phoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ShippingEntity;
import project.phoneshop.model.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface ShippingRepository extends JpaRepository<ShippingEntity, UUID> {
    Optional<ShippingEntity> findByOrderShipping(OrderEntity order);
    Optional<ShippingEntity> findByUserOrderShippingAndOrderShipping(UserEntity user, OrderEntity order);
}
