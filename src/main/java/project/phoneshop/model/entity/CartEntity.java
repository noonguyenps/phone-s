package project.phoneshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestResource(exported = false)
@Entity
@Table(name = "\"carts\"")
public class CartEntity {
    @Id
    @Column(name = "\"cart_id\"")
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"user_id\"")
    private UserEntity userCart;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"product_id\"")
    private ProductEntity productCart;
    @Column(name = "\"quantity\"")
    private int quantity;
    @Column(name = "\"status\"")
    private Boolean status;
    @Column(name = "\"active\"")
    private Boolean active;
    @JsonIgnore
    @ElementCollection
    private Set<String> listAttributeOption;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"order_id\"")
    private OrderEntity order;
    public CartEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUserCart() {
        return userCart;
    }

    public void setUserCart(UserEntity userCart) {
        this.userCart = userCart;
    }

    public ProductEntity getProductCart() {
        return productCart;
    }

    public void setProductCart(ProductEntity productCart) {
        this.productCart = productCart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Set<String> getListAttributeOption() {
        return listAttributeOption;
    }

    public void setListAttributeOption(Set<String> listAttributeOption) {
        this.listAttributeOption = listAttributeOption;
    }
}
