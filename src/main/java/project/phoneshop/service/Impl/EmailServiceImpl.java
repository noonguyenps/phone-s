package project.phoneshop.service.Impl;

import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.security.JWT.JwtUtils;
import project.phoneshop.service.EmailService;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private Configuration config;
    @Value("${apps.server.host}")
    private String host;


    @Override
    public void sendForgetPasswordMessage(UserEntity user) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String subject = "Reset password";
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Template template= config.getTemplate("email-temp.ftl");
            Map<String, Object> model=new HashMap<>();
            model.put("fullname",user.getFullName());
            model.put("link",host+"?token="+jwtUtils.generateEmailJwtToken(user.getEmail()));
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            System.out.println(html);
            helper = new MimeMessageHelper(message, true);
            helper.setFrom("noreply@baeldung.com");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(html,true);
            javaMailSender.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void sendmail(UserEntity user) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("phone.s.shop.2412@gmail.com", "sigifbltnkjlidto");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("phone.s.shop.2412@gmail.com", false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        msg.setSubject("Reset password");
        msg.setContent("Click link to reset password: "+ host+"?token="+jwtUtils.generateEmailJwtToken(user.getEmail()), "text/html");
        msg.setSentDate(new Date());
        Transport.send(msg);
    }

}

