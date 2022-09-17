package project.phoneshop.model.payload.request.product;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UpdateCategoryRequest {
    @NotEmpty(message = "Tên category không được để trống")
    String name;
    @NotEmpty(message = "Category parent không được để trống")
    String parent;
}
