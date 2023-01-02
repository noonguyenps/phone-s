package project.phoneshop.model.payload.response.order;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.phoneshop.model.entity.AddressEntity;
import project.phoneshop.model.entity.PaymentEntity;
import project.phoneshop.model.entity.ShipEntity;
import project.phoneshop.model.entity.VoucherEntity;
import project.phoneshop.model.payload.response.cart.CartResponseFE;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Setter
@Getter
public class OrderResponse {
    private int orderId;
    private Date createdDate;
    private int orderStatus;
    private Date expectedDate;
    private Double total;
    private String name;
    private AddressEntity addressOrder;
    private ShipEntity shipOrder;
    private PaymentEntity paymentOrder;
    private List<CartResponseFE> cartResponseFEs;
    private VoucherEntity voucherOrder;
    private boolean paymentStatus;
}
