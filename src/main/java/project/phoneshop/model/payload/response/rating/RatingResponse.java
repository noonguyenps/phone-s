package project.phoneshop.model.payload.response.rating;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.phoneshop.model.payload.response.product.ProductResponse;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Setter
@Getter
public class RatingResponse {

    private int id;
    private String comment;
    private List<String> urls;
    private int star;
    private String nickname;
    private ProductResponse productResponse;
    private List<Map<String,Object>> comments;
}
