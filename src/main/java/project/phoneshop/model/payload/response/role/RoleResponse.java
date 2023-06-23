package project.phoneshop.model.payload.response.role;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Data
@NoArgsConstructor
@Setter
@Getter
public class RoleResponse {
    private UUID id;
    private String name;
}
