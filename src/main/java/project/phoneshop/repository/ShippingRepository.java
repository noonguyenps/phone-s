package project.phoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.phoneshop.model.entity.ShippingEntity;

import java.util.UUID;

public interface ShippingRepository extends JpaRepository<ShippingEntity, UUID> {
}
