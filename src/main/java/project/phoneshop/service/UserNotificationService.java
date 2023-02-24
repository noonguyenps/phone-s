package project.phoneshop.service;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.entity.UserNotificationEntity;

import java.util.List;
import java.util.UUID;


@Component
@Service
public interface UserNotificationService {
    List<UserNotificationEntity> getAll();

    void saveNotification(UserNotificationEntity notification);

    UserNotificationEntity findNotificationById(int notification_id);

    List<UserNotificationEntity> findNotificationByUser(UserEntity user);

    List<UserNotificationEntity> findNotificationByUser(UserEntity user, int page, int size, String type);

    UserNotificationEntity create(UserNotificationEntity notification, UserEntity user);
    void delete(int notification_id);
}
