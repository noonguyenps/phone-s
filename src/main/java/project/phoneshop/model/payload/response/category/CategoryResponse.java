package project.phoneshop.model.payload.response.category;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Data
@NoArgsConstructor
@Setter
@Getter
public class CategoryResponse {
    private UUID id;
    private String name;
    private UUID parentId;
    private String parentName;

    public CategoryResponse(UUID id, String name, UUID parent, String parentName) {
        this.id = id;
        this.name = name;
        this.parentId = parent;
        this.parentName = parentName;
    }
}
