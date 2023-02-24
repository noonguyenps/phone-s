package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.entity.UserNotificationEntity;
import project.phoneshop.repository.UserNotificationRepository;
import project.phoneshop.repository.UserRepository;
import project.phoneshop.service.UserNotificationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class UserNotificationServiceImpl implements UserNotificationService {

    final UserNotificationRepository userNotificationRepository;
    final UserRepository userRepository;


    public List<UserNotificationEntity> getAll() {
        return userNotificationRepository.findAll();
    }
    @Override
    public void saveNotification(UserNotificationEntity notification){
        userNotificationRepository.save(notification);
    }

    @Override
    public UserNotificationEntity findNotificationById(int notification_id) {
        Optional<UserNotificationEntity> notification = userNotificationRepository.findByNotificationId(notification_id);
        if(notification.isEmpty()){
            return null;
        }
        return notification.get();
    }


    @Override
    public List<UserNotificationEntity> findNotificationByUser(UserEntity user) {
        if(userNotificationRepository.findAllByUser(user).isEmpty()){
            return null;
        }else {
            return userNotificationRepository.findAllByUser(user);
        }
    }
    @Override
    public List<UserNotificationEntity> findNotificationByUser(UserEntity user, int page, int size, String type){
        Pageable paging = PageRequest.of(page, size);
        Page<UserNotificationEntity> pagedResult = userNotificationRepository.findAllByUser(user.getId(),type,paging);
        return pagedResult.toList();
    }

    @Override
    public UserNotificationEntity create(UserNotificationEntity notification, UserEntity user) {
        notification.setUser(user);
        return userNotificationRepository.save(notification);
    }

    @Override
    public void delete(int id) {
        userNotificationRepository.deleteById(id);
    }
}
