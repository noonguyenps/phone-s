package project.phoneshop.model.payload.response.product;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.phoneshop.model.entity.AttributeDetailEntity;
import project.phoneshop.model.entity.AttributeOptionEntity;
import project.phoneshop.model.entity.ProductAttributeOptionDetail;

import java.util.*;

@Data
@NoArgsConstructor
@Setter
@Getter
public class ProductResponse {
    private UUID id;
    private String image;
    private String name;
    private String description;
    private double rate;
    private double price;
    private double discount;
    private int sold;
    private int inventory;
    private String brand;
    private String origins;
    private String category;
    private List<String> img;
    private Set<Map<String,Object>> listAttributeOption;
    private Date createAt;
    private int status;
    private Set<Map<String,Object>> attributeDetailEntityList;
    private UUID categoryRoot;

    public ProductResponse(UUID id, String image, String name, String description, String category, double rate, double price, double discount, int sold, int inventory,String brand, String origins, List<String> img, Set<Map<String,Object>> listAttributeOption,Date createAt, int status, UUID categoryRoot) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.category = category;
        this.rate = rate;
        this.price = price;
        this.discount = discount;
        this.sold = sold;
        this.brand = brand;
        this.origins = origins;
        this.img = img;
        this.inventory = inventory;
        this.listAttributeOption = listAttributeOption;
        this.createAt = createAt;
        this.status =status;
    }
    public ProductResponse(UUID id, String image, String name, String description, String category, double rate, double price, double discount, int sold, int inventory,String brand, String origins, List<String> img, Set<Map<String,Object>> listAttributeOption,Date createAt, int status, Set<Map<String,Object>> attributeDetailEntityList, UUID categoryRoot) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.category = category;
        this.rate = rate;
        this.price = price;
        this.discount = discount;
        this.sold = sold;
        this.brand = brand;
        this.origins = origins;
        this.img = img;
        this.inventory = inventory;
        this.listAttributeOption = listAttributeOption;
        this.createAt = createAt;
        this.status =status;
        this.attributeDetailEntityList = attributeDetailEntityList;
        this.categoryRoot = categoryRoot;
    }
}
