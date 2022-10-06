package project.phoneshop.model.payload.request.product;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UpdateProductRequest {
    @NotEmpty(message = "Name is Not Empty")
    private String name;
    @NotEmpty(message = "Brand is Not Empty")
    private UUID brand;
    @NotEmpty(message = "Category is Not Empty")
    private UUID category;
    private String description;
    private double discount;
    @NotEmpty(message = "Price is Not Empty")
    private Double price;
    @NotEmpty(message = "Inventory is Not Empty")
    @Min(0)
    private int inventory;
}