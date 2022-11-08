package project.phoneshop.model.payload.request.product;

import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class AddProductRequest {
    private String name;
    private UUID brand;
    private UUID category;
    private String description;
    private double discount;
    private Double price;
    private Integer inventory;
    List<String> attribute;
    List<Double> values;
    List<String> imgUrl;
}
