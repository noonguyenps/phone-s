package project.phoneshop.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.phoneshop.model.entity.VoucherEntity;
import project.phoneshop.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface VoucherRepository extends JpaRepository<VoucherEntity, UUID> {
    List<VoucherEntity> findAllByType(String type);
    List<VoucherEntity> findAllByStatus(boolean status);
    Optional<VoucherEntity> findByIdAndUserEntities(UUID id, UserEntity user);
    List<VoucherEntity> findByUserEntities(UserEntity user);

}
