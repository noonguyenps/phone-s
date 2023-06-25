package project.phoneshop.model.payload.request.authentication;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReActiveRequest {
    String data;
}
