package project.phoneshop.service;

import org.springframework.stereotype.Component;
import project.phoneshop.model.entity.UserEntity;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;

@Component
public interface EmailService {
    public void sendForgetPasswordMessage(UserEntity user);

    void sendmail(UserEntity user) throws AddressException, MessagingException, IOException;
}

