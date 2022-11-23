package project.phoneshop.model.payload.request.order;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddNewOrderRequest {
    private List<UUID> listCart;
    private UUID address;
    private int payment;
    private int ship;
    private UUID voucher;

}
