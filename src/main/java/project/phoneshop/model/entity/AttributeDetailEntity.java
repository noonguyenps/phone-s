package project.phoneshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;

@RestResource(exported = false)
@Entity
@Table(name = "\"attribute_details\"")
public class AttributeDetailEntity {
    @Id
    @Column(name = "\"attribute_detail_id\"")
    private String id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "\"attribute_id\"", referencedColumnName = "\"attribute_id\"")
    private AttributeEntity idTypeDetail;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "\"product_id\"", referencedColumnName = "\"product_id\"")
    private ProductEntity product;
    @Column(name = "\"value\"")
    private String value;

    public AttributeDetailEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttributeEntity getIdTypeDetail() {
        return idTypeDetail;
    }

    public void setIdTypeDetail(AttributeEntity idTypeDetail) {
        this.idTypeDetail = idTypeDetail;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
