package project.phoneshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;
import project.phoneshop.model.entity.UserEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestResource(exported = false)
@Entity
@Table(name = "\"voucher\"")
public class VoucherEntity {
    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "voucher_id")
    private UUID id;
    @Column(name = "amount")
    private int amount;
    @Column(name = "status")
    private boolean status;
    @Column(name = "value")
    private String value;
    @Column(name = "type")
    private String type;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "from_date")
    private Date fromDate;
    @Column(name = "to_date")
    private Date toDate;
    @Column(name = "expired_date")
    private Date expiredDate;
    @JsonIgnore
    @OneToMany(mappedBy = "voucherOrder",cascade = CascadeType.ALL)
    private List<OrderEntity> listOrder;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "\"user_voucher\"", joinColumns = @JoinColumn(name = "\"voucher_id\""), inverseJoinColumns = @JoinColumn(name = "\"user_id\""))
    private Set<UserEntity> userEntities;
    public VoucherEntity() {}
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public Set<UserEntity> getUserEntities() {
        return userEntities;
    }
    public void setUserEntities(Set<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getFromDate() {
        return fromDate;
    }
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Date getToDate() {
        return toDate;
    }
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
    public Date getExpiredDate() {
        return expiredDate;
    }
    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public List<OrderEntity> getListOrder() {
        return listOrder;
    }

    public void setListOrder(List<OrderEntity> listOrder) {
        this.listOrder = listOrder;
    }
}
