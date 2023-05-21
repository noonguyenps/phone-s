package project.phoneshop.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.phoneshop.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query(value = "SELECT * FROM user_role s, roles r, users u WHERE s.role_id = r.id AND u.user_id=s.user_id AND r.name='SHIPPER'",
    countQuery = "SELECT * FROM user_role s, roles r, users u WHERE s.role_id = r.id AND u.user_id=s.user_id AND r.name='SHIPPER'",
    nativeQuery = true)
    Page<UserEntity> findAllShipper(Pageable pageable);
    @Query(value = "SELECT * FROM user_role s, roles r, users u WHERE s.role_id = r.id AND u.user_id=s.user_id AND r.name='MANAGER'",
            nativeQuery = true)
    Page<UserEntity> findAllManager(Pageable pageable);
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByPhone(String phone);
    Optional<UserEntity> findByPhone(String phone);
    @Query(value = "select extract(month from create_at) as mon,extract(year from create_at) as yyyy,count(user_id) as summ from users group by 1,2",
            nativeQuery = true)
    List<Object> countUserPerMonth();
}
