package project.phoneshop.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.response.CountPerMonth;
import project.phoneshop.model.payload.response.user.UserResponse;
import project.phoneshop.model.payload.response.user.UserResponseAdmin;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface UserService {
    int getCountUser();

    //    UserEntity findByFullName(String fullname);
    UserEntity findById(UUID id);
    List<UserEntity> getAllUser(int page, int size);
    List<UserEntity> getAllManager(int page, int size);
    List<UserEntity> getAllShipper(int page, int size);
    UserEntity saveUser(UserEntity user,String roleName);
    UserEntity findByPhone(String phone);
    Boolean existsByPhone(String phone);
    UserEntity saveInfo(UserEntity user);
    UserEntity findByEmail(String email);
    List<Object> countUserPerMonth();
    UserEntity updateActive(UserEntity user);
    UserResponse getUserResponse(UserEntity user);

    UserResponseAdmin getUserResponseAdmin(UserEntity user);
}
