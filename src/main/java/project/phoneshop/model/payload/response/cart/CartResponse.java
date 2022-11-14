package project.phoneshop.model.payload.response.cart;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;
@Data
@NoArgsConstructor
@Setter
@Getter
public class CartResponse {
    private UUID idCart;
    private UUID productId;
    private String productName;
    private String img;
    private double price;
    private int quantity;
    private Boolean status;
    private Boolean active;
    private List<Map<String,Object>> listAttributeOption;

    public CartResponse(UUID idCart, UUID productId, String productName, String img, double price, int quantity, Boolean status, Boolean active, List<Map<String, Object>> listAttributeOption) {
        this.idCart = idCart;
        this.productId = productId;
        this.productName = productName;
        this.img = img;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.active = active;
        this.listAttributeOption = listAttributeOption;
    }
}
