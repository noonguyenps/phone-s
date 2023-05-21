package project.phoneshop.model.payload.request.user;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UpdatePhonePassword {
    private String phone;
    private String password;
    private String retypePassword;
}
