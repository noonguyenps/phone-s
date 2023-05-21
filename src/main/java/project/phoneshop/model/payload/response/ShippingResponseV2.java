package project.phoneshop.model.payload.response.shipping;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Setter
@Getter
public class ShippingResponseV2 {

    @Data
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Cart{
        String productName;
        String productImage;
        int quantity;

    }
    UUID id;
    String image1;
    String image2;
    String image3;
    int state;
    int orderID;
    List<Cart> carts;
    String orderName;
    String customerName;
    String customerPhone;
    String addressDetail;
    String commune;
    String district;
    String province;
    double total;
    boolean statusPayment;
    int statusOrder;
}
