package project.phoneshop.model.payload.request.cart;

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
public class AddNewCartRequest {
    private UUID productId;
    private int quantity;
    private List<String> listAttribute;
}
