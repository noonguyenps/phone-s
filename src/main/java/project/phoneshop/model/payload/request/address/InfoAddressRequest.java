package project.phoneshop.model.payload.request.address;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@NoArgsConstructor
@Getter
@Setter
@Data
public class InfoAddressRequest {
    @NotEmpty(message = "Fullname can't be empty")
    private String fullName;
    private String companyName;
    @NotEmpty(message = "Phone number can't be empty")
    private String phone;
    @NotEmpty(message = "Province can't be empty")
    private String province;
    @NotEmpty(message = "District can't be empty")
    private String district;
    @NotEmpty(message = "Commune can't be empty")
    private String commune;
    @NotEmpty(message = "Address Detail can't be empty")
    private String addressDetail;
    @NotNull(message = "Address type can't be empty")
    private int addressType;
}
