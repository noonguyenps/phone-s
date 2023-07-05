package project.phoneshop.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@RestResource(exported = false)
@Table(name = "\"category\"")
public class CategoryEntity {
    @Id
    @JsonIgnore
    @Column(name = "\"category_id\"")
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(name = "\"category_name\"", nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name="\"parent_id\"",referencedColumnName="\"category_id\"")
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
    private Collection<CategoryEntity> categoryEntities;
    @JsonIgnore
    @OneToMany(mappedBy = "productCategory",cascade = CascadeType.ALL)
    private List<ProductEntity> listProduct;

    public CategoryEntity() {
    }

    public CategoryEntity(String name, CategoryEntity category) {
        this.name = name;
        this.parent = category;
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

    public CategoryEntity getParent() {
        return parent;
    }

    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }

    @JsonIgnore
    public Collection<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(Collection<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<ProductEntity> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductEntity> listProduct) {
        this.listProduct = listProduct;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent='" + parent + '\'' +
                '}';
    }
}