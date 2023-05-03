package project.phoneshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestResource(exported = false)
@Entity
@Table(name = "\"orders\"")
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"order_id\"")
    private int orderId;
    @ManyToOne
    @JoinColumn(name="\"user_order\"")
    private UserEntity userOrder;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<CartEntity> cartOrder;
    @Column(name = "\"name\"")
    private String name;
    @ManyToOne
    @JoinColumn(name="\"user_address\"")
    private AddressEntity addressOrder;
    @Column(name = "\"created_date\"")
    private Date createdDate;
    @ManyToOne
    @JoinColumn(name = "\"payment_type\"")
    private PaymentEntity paymentOrder;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "\"ship_type\"")
    private ShipEntity shipOrder;

    @Column(name = "\"total\"")
    private double total;

    @Column(name = "\"del_status\"")
    private int delStatus;

    @Column(name = "\"expected_date\"")
    private Date expectedDate;
    @Column(name ="\"status_payment\"")
    private Boolean statusPayment;
    @Column(name ="\"status\"")
    private int orderStatus;
    @JsonIgnore
    @Column(name ="\"secret_key\"")
    private String secretKey;
    @ManyToOne
    @JoinColumn(name="\"voucher_order\"")
    private VoucherEntity voucherOrder;

    @JsonIgnore
    @OneToMany(mappedBy = "orderShipping",cascade = CascadeType.ALL)
    private List<ShippingEntity> shippingEntities;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressEntity getAddressOrder() {
        return addressOrder;
    }

    public void setAddressOrder(AddressEntity addressOrder) {
        this.addressOrder = addressOrder;
    }

    public PaymentEntity getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentEntity paymentOrder) {
        this.paymentOrder = paymentOrder;
    }

    public ShipEntity getShipOrder() {
        return shipOrder;
    }

    public void setShipOrder(ShipEntity shipOrder) {
        this.shipOrder = shipOrder;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public List<CartEntity> getCartOrder() {
        return cartOrder;
    }

    public void setCartOrder(List<CartEntity> cartOrder) {
        this.cartOrder = cartOrder;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Boolean getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(Boolean statusPayment) {
        this.statusPayment = statusPayment;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public UserEntity getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(UserEntity userOrder) {
        this.userOrder = userOrder;
    }

    public VoucherEntity getVoucherOrder() {
        return voucherOrder;
    }

    public void setVoucherOrder(VoucherEntity voucherOrder) {
        this.voucherOrder = voucherOrder;
    }

    public List<ShippingEntity> getShippingEntities() {
        return shippingEntities;
    }

    public void setShippingEntities(List<ShippingEntity> shippingEntities) {
        this.shippingEntities = shippingEntities;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
