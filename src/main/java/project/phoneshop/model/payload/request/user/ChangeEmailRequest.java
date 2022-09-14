package project.phoneshop.model.payload.request.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class ChangeEmailRequest {
    @NotEmpty(message = "Thiếu Email")
    @Email(regexp = ".+@.+\\..+",message = "Email không hợp lệ")
    private String email;
}
