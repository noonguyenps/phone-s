package project.phoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.phoneshop.model.entity.ProductAttributeOptionDetail;

import java.util.UUID;

public interface ProductAttributeOptionDetailRepository extends JpaRepository<ProductAttributeOptionDetail, UUID> {
}
