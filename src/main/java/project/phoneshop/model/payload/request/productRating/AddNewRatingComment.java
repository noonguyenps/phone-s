package project.phoneshop.model.payload.request.productRating;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@NoArgsConstructor
public class AddNewRatingComment {
    @NotEmpty(message = "Comment can't empty")
    private String comment;
}
