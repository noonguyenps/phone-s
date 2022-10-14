package project.phoneshop.service;

import org.springframework.stereotype.Component;
import project.phoneshop.model.entity.UserEntity;

@Component
public interface EmailService {
    public void sendForgetPasswordMessage(UserEntity user);

}

