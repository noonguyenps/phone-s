package project.phoneshop.model.payload.response.cart;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Setter
@Getter
public class CartResponseFE {
    private boolean choose;
    private List<String> option;
    private Set<String> optionId;
    private UUID id;
    private String name;
    private String img;
    private UUID productId;
    private Double price;
    private int quantity;

    public CartResponseFE(boolean choose, List<String> option, Set<String> optionId, UUID id, String name, String img, UUID productId, Double price, int quantity) {
        this.choose = choose;
        this.option = option;
        this.optionId = optionId;
        this.id = id;
        this.name = name;
        this.img = img;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }
}
