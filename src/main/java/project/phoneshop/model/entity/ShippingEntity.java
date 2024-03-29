package project.phoneshop.model.entity;


import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "\"shipping\"")
@RestResource(exported = false)
public class ShippingEntity {
    @Id
    @Column(name = "\"shipping_id\"")
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(name = "\"shipper_name\"")
    private String shipperName;
    @Column(name = "\"shipper_id\"")
    private String shipperID;
    @Column(name = "\"shipper_phone\"")
    private String shipperPhone;
    @Column(name = "\"image_1\"")
    private String image1;
    @Column(name = "\"image_2\"")
    private String image2;
    @Column(name = "\"image_3\"")
    private String image3;
    @Column(name = "\"state\"")
    private int state;
    @Column(name = "\"create_at\"")
    private Date create;
    @Column(name = "\"update_at\"")
    private Date update;
    @ManyToOne
    @JoinColumn(name="\"user_shipping\"")
    private UserEntity userOrderShipping;
    @OneToOne
    @JoinColumn(name="\"order_id\"")
    private OrderEntity orderShipping;


    public ShippingEntity() {
    }

    public ShippingEntity(UUID id, String shipperName, String shipperID, String shipperPhone, int state, Date create, Date update, UserEntity userOrderShipping, OrderEntity orderShipping) {
        this.id = id;
        this.shipperName = shipperName;
        this.shipperID = shipperID;
        this.shipperPhone = shipperPhone;
        this.state = state;
        this.create = create;
        this.update = update;
        this.userOrderShipping = userOrderShipping;
        this.orderShipping = orderShipping;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperID() {
        return shipperID;
    }

    public void setShipperID(String shipperID) {
        this.shipperID = shipperID;
    }

    public String getShipperPhone() {
        return shipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        this.shipperPhone = shipperPhone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public UserEntity getUserOrderShipping() {
        return userOrderShipping;
    }

    public void setUserOrderShipping(UserEntity userOrderShipping) {
        this.userOrderShipping = userOrderShipping;
    }

    public OrderEntity getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(OrderEntity orderShipping) {
        this.orderShipping = orderShipping;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
