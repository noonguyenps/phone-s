package project.phoneshop.model.payload.request.user;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotEmpty
    @Min(value = 6, message = "Password must be at least 8 characters")
    private String newPassword;
    @NotEmpty
    @Min(value = 6, message = "Password must be at least 8 characters")
    private String confirmPassword;
}
