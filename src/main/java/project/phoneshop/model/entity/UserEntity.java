package project.phoneshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;
import project.phoneshop.model.entity.RoleEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestResource(exported = false)
@Entity
@Table(name = "\"users\"")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(name = "\"full_name\"")
    private String fullName;
    @Column(name = "\"email\"")
    private String email;
    @JsonIgnore
    @Column(name = "\"password\"")
    private String password;
    @Column(name = "\"gender\"")
    private String gender;
    @Column(name = "\"nick_name\"")
    private String nickName;
    @Column(name = "\"phone\"")
    private String phone;
    @Column(name = "\"birthDate\"")
    private Date birthDate;
    @Column(name = "\"img\"")
    private String img;
    @Column(name = "\"status\"")
    private boolean status;
    @Column(name = "\"active\"")
    private boolean active;
    @Column(name = "\"country\"")
    private String country;
    @Column(name = "\"createAt\"")
    private Date createAt;
    @Column(name = "\"updateAt\"")
    private Date updateAt;
    @Column(name = "\"facebookAuth\"")
    private Boolean facebookAuth;
    @Column(name = "\"googleAuth\"")
    private Boolean googleAuth;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "\"user_role\"", joinColumns = @JoinColumn(name = "\"user_id\""), inverseJoinColumns = @JoinColumn(name = "\"role_id\""))
    private Set<RoleEntity> roles;
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<AddressEntity> listAddress;
    @JsonIgnore
    @OneToMany(mappedBy = "userCart",cascade = CascadeType.ALL)
    private List<CartEntity> listCart;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "\"favorite_list\"",
            joinColumns = @JoinColumn(name = "\"user_id\""),
            inverseJoinColumns = @JoinColumn(name = "\"product_id\""))
    private List<ProductEntity> favoriteProducts;
    public UserEntity() {
    }
    public UserEntity(String phone,String password){
        this.phone = phone;
        this.password = password;
        this.active = false;
        this.status = true;
        this.createAt= new Date();

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Date getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Boolean getFacebookAuth() {
        return facebookAuth;
    }

    public void setFacebookAuth(Boolean facebookAuth) {
        this.facebookAuth = facebookAuth;
    }

    public Boolean getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(Boolean googleAuth) {
        this.googleAuth = googleAuth;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public List<AddressEntity> getAddress() {
        return listAddress;
    }

    public void setAddress(List<AddressEntity> address) {
        this.listAddress = address;
    }

    public List<AddressEntity> getListAddress() {
        return listAddress;
    }

    public void setListAddress(List<AddressEntity> listAddress) {
        this.listAddress = listAddress;
    }

    public List<CartEntity> getListCart() {
        return listCart;
    }

    public void setListCart(List<CartEntity> listCart) {
        this.listCart = listCart;
    }

    public List<ProductEntity> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void setFavoriteProducts(List<ProductEntity> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }
}
