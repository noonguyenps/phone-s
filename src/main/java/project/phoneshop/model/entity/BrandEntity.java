package project.phoneshop.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@RestResource(exported = false)
@Entity
@Table(name = "\"brands\"")
public class BrandEntity {
    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "\"brand_id\"")
    private UUID id;
    @Column(name = "\"brand_name\"")
    private String name;
    @Column(name = "\"brand_description\"")
    private String description;
    @Column(name="\"country_id\"")
    private String brandCountry;
    @Column(name = "\"brand_phone\"")
    private String phone;
    @Column(name="\"commune_id\"")
    private String brandCommune;
    @Column(name="\"district_id\"")
    private String brandDistrict;
    @Column(name="\"province_id\"")
    private String brandProvince;
    @JsonIgnore
    @OneToMany(mappedBy = "productBrand",cascade = CascadeType.ALL)
    private List<ProductEntity> listProduct;
    @Column(name = "\"address_details\"")
    private String addressDetails;
    @Column(name = "\"logo\"")
    private String img;
    @Column(name = "year_create")
    private Integer yearCreate;

    public BrandEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrandCountry() {
        return brandCountry;
    }

    public void setBrandCountry(String brandCountry) {
        this.brandCountry = brandCountry;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBrandCommune() {
        return brandCommune;
    }

    public void setBrandCommune(String brandCommune) {
        this.brandCommune = brandCommune;
    }

    public String getBrandDistrict() {
        return brandDistrict;
    }

    public void setBrandDistrict(String brandDistrict) {
        this.brandDistrict = brandDistrict;
    }

    public String getBrandProvince() {
        return brandProvince;
    }

    public void setBrandProvince(String brandProvince) {
        this.brandProvince = brandProvince;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getYearCreate() {
        return yearCreate;
    }

    public void setYearCreate(Integer yearCreate) {
        this.yearCreate = yearCreate;
    }

    public List<ProductEntity> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductEntity> listProduct) {
        this.listProduct = listProduct;
    }
}
