package project.phoneshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.phoneshop.model.entity.AttributeDetailEntity;

@EnableJpaRepositories
public interface AttributeDetailRepository extends JpaRepository<AttributeDetailEntity,Integer> {
}
