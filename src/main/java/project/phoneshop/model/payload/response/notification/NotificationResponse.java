package project.phoneshop.model.payload.response.notification;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Setter
@Getter
public class NotificationResponse {
    private int id;
    private UUID userId;
    private String type;
    private String text;
    private boolean seen;
    private Date date;
    private String link;
    private Date createdAt;
    private Date updatedAt;
}
