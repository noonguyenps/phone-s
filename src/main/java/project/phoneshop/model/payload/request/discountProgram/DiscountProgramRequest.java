package project.phoneshop.model.payload.request.discountProgram;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import project.phoneshop.util.DateCheck;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Getter
@NoArgsConstructor
@ToString
@DateCheck(message = "from_date must be before to_date")
public class DiscountProgramRequest {
    @NotEmpty(message = "must have name")
    private String name;
    @NotNull(message="Not empty")
    private Float percent;
    @NotNull(message="Not empty")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date fromDate;
    @NotNull(message="Not empty")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date toDate;
    @NotEmpty(message="Not empty")
    private String description;
}
