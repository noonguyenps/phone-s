package project.phoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.phoneshop.model.entity.CartEntity;
import project.phoneshop.model.entity.ProductEntity;
import project.phoneshop.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@EnableJpaRepositories
public interface CartRepository extends JpaRepository<CartEntity, UUID> {
    @Override
    Optional<CartEntity> findById(UUID uuid);
    List<CartEntity> findByUserCart(UserEntity entity);
    List<CartEntity> findByUserCartAndProductCart(UserEntity user, ProductEntity product);
    void deleteCartById(UUID id);
}
