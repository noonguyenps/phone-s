package project.phoneshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.phoneshop.model.entity.BrandEntity;

import java.util.UUID;

@EnableJpaRepositories
public interface BrandRepository extends JpaRepository<BrandEntity, UUID> {
    @Query(value = "SELECT * FROM brands WHERE converttvkdau(LOWER(brands.brand_name)) LIKE %?1% OR converttvkdau(LOWER(brands.brand_name)) LIKE %?2%",
            countQuery = "SELECT * FROM brands WHERE converttvkdau(LOWER(brands.brand_name)) LIKE %?1%  OR converttvkdau(LOWER(brands.brand_name)) LIKE %?2%",
            nativeQuery = true)
    Page<BrandEntity> findByKeyword(String keyword1, String keyword2, Pageable pageable);

}
