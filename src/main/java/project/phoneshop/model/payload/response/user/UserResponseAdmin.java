package project.phoneshop.model.payload.response.user;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.phoneshop.model.entity.AddressEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Setter
@Getter
public class UserResponseAdmin {
    private UUID id;
    private String fullName;
    private String email;
    private String gender;
    private String nickName;
    private String phone;
    private Date birthDate;
    private String img;
    private Boolean status;
    private Boolean active;
    private String country;
    private Date createAt;
    private Date updateAt;
    private Boolean facebookAuth;
    private Boolean googleAuth;
    List<AddressEntity> address;
    private double countOrderTotal;
    private int countOrder;
    private int countVoucher;
    private int countProductFavorite;
    private UUID roleId;
    private String role;
}
