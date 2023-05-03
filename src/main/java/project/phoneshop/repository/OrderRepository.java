package project.phoneshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ProductEntity;
import project.phoneshop.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity,Integer> {
    OrderEntity findByName(String name);
    Optional<OrderEntity> findByOrderId(int id);
    @Query(value = "SELECT * FROM products WHERE LOWER(products.product_name) LIKE %?1%",
            countQuery = "SELECT * FROM products WHERE LOWER(products.product_name) LIKE %?1%",
            nativeQuery = true)
    OrderEntity getOderByUser(UserEntity user);
    @Query(value = "SELECT SUM(orders.total) FROM orders",
            countQuery = "SELECT SUM(orders.total) FROM orders",
            nativeQuery = true)
    int countPrice();

    @Query(value = "select extract(month from created_date) as mon,extract(year from created_date) as yyyy,count(order_id) as summ, sum(total) from orders group by 1,2",
    nativeQuery = true)
    List<Object> countOrderPerMonth();
    @Query(value = "SELECT * FROM orders",
            countQuery = "SELECT count(*) FROM orders",
            nativeQuery = true)
    Page<OrderEntity> findAllOrder(Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE orders.status=?1",
            countQuery = "SELECT count(*) FROM orders WHERE orders.status=?1",
            nativeQuery = true)
    Page<OrderEntity> findAllOrderByStatus(int status,Pageable pageable);

    OrderEntity findByOrderIdAndSecretKey(int orderId,String secretKey);
}
