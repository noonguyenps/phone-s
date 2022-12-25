package project.phoneshop.model.payload.request.productRating;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class AddNewRatingRequest {
    @NotNull(message = "Rating point can't empty")
    @Min(value = 1,message = ">=1")
    @Max(value = 5,message = "<=5")
    private int ratingPoint;
    @NotEmpty(message = "Message can't empty")
    private String message;
    private List<String> imgUrl;
}
