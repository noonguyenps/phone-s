package project.phoneshop.mapping;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.entity.UserNotificationEntity;
import project.phoneshop.model.payload.request.notification.AddNotificationRequest;
import project.phoneshop.model.payload.response.notification.NotificationResponse;
import project.phoneshop.service.UserNotificationService;
import project.phoneshop.service.UserService;
import java.util.Date;

@Component
public class UserNotificationMapping {
    @Autowired
    UserNotificationService userNotificationService;
    @Autowired
    UserService userService;
    public UserNotificationEntity modelToEntity(AddNotificationRequest addNotificationRequest){
        UserNotificationEntity newNotification = new UserNotificationEntity();
        UserEntity user = userService.findById(addNotificationRequest.getIdUser());
        newNotification.setType(addNotificationRequest.getType());
        newNotification.setUser(user);
        newNotification.setStatus(1);
        newNotification.setDateCreate(new Date());
        newNotification.setMessage(addNotificationRequest.getMessage());
        return newNotification;
    }
    public NotificationResponse entityToResponse(UserNotificationEntity notification){
        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setId(notification.getNotificationId());
        notificationResponse.setUserId(notification.getUser().getId());
        notificationResponse.setLink("");
        if(notification.getStatus()==1)
            notificationResponse.setSeen(false);
        else notificationResponse.setSeen(true);
        notificationResponse.setText(notification.getMessage());
        Date date = new Date();
        notificationResponse.setDate(date);
        notificationResponse.setCreatedAt(date);
        notificationResponse.setType(notification.getType());
        notificationResponse.setUpdatedAt(date);
        return notificationResponse;
    }
}
