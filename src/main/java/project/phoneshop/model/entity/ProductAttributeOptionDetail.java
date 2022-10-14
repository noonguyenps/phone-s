package project.phoneshop.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.UUID;
@RestResource(exported = false)
@Entity
@Table(name = "\"attribute_options_detail\"")
public class ProductAttributeOptionDetail {
    @Id
    @Column(name = "\"id\"")
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
    @JoinColumn(name = "\"product_id\"")
    private ProductEntity productAttribute;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"attribute_option_id\"")
    private AttributeOptionEntity attributeOption;
    @Column(name = "value")
    private double value;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ProductEntity getProductCart() {
        return productAttribute;
    }

    public void setProductCart(ProductEntity productAttribute) {
        this.productAttribute = productAttribute;
    }

    public AttributeOptionEntity getAttributeOption() {
        return attributeOption;
    }

    public void setAttributeOption(AttributeOptionEntity attributeOption) {
        this.attributeOption = attributeOption;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
