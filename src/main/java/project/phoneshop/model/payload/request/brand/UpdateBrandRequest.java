package project.phoneshop.model.payload.request.brand;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
@Data
@NoArgsConstructor
@Setter
@Getter
public class UpdateBrandRequest {
    private String name;
    private String phone;
    private String country;
    private String province;
    private String district;
    private String commune;
    private String addressDetails;
    private String description;
    private String img;
}
