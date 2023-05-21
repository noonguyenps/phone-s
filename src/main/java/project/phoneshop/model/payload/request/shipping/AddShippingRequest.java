package project.phoneshop.model.payload.request.shipping;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddShippingRequest {
    UUID shipperID;
    int order;
}
