package project.phoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.response.CountPerMonth;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByPhone(String phone);
    Optional<UserEntity> findByPhone(String phone);
    @Query(value = "select extract(month from create_at) as mon,extract(year from create_at) as yyyy,count(user_id) as summ from users group by 1,2",
            nativeQuery = true)
    List<Object> countUserPerMonth();
}
