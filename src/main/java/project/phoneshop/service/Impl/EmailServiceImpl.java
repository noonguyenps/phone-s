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
        msg.setSubject("SPhone Reset password");
        msg.setContent("<!DOCTYPE html>\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"vi\" xml:lang=\"vi\">\n" +
                        "  <head>\n" +
                        "    <meta charset=\"utf-8\" />\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width\" />\n" +
                        "    <title>Password Reset</title>\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <div style=\"background-color: rgb(226, 223, 223); width: 100%;height: 100%;\">\n" +
                        "        <center>\n" +
                        "            <img src=\"https://res.cloudinary.com/duk2lo18t/image/upload/v1665719834/frontend/S-Phone_cpfelx.png\" width=\"200\" height=\"200\" style=\"margin: 1%;border-radius: 1rem;\"/>\n" +
                        "            <div>\n" +
                        "                <div style=\"width: 700px; border-radius: 2%; background-color: rgb(250, 250, 250); padding: 10px;margin: 10px;\">\n" +
                        "                    <h1>Reset Password</h1>\n" +
                        "                    <h4>Hello. We, S-Phone customer service have received your password reset request. If you've forgotten your password, don't worry, click the link below to reset your password.</h4>\n" +
                        "                    <a href=\""+host+"?token="+jwtUtils.generateEmailJwtToken(user.getEmail())+"\"><div style=\"width: 150px; height: 50px; border-radius: 1rem; background-color: rgb(98, 130, 219); padding: 10px;margin: 10px;\">\n" +
                        "                        <p>Reset My Password</p>\n" +
                        "                    </div>\n" +
                        "                    \n" +
                        "                    </a>\n" +
                        "                </div>\n" +
                        "                <h5>If not you? Please contact us.</h4>\n" +
                        "                <h5>Tel: 0868704516 - Email: phone.s.shop.2412@gmail.com</h4>\n" +
                        "                <br/>\n" +
                        "            </div>\n" +
                        "        </center>\n" +
                        "        \n" +
                        "    </div>\n" +
                        "  </body>\n" +
                        "</html>"
                , "text/html");
        msg.setSentDate(new Date());
        Transport.send(msg);
    }

    @Override
    public void sendmailVerification(UserEntity user, String email) throws AddressException, MessagingException, IOException {
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
        msg.setSubject("SPhone Reset password");
        msg.setContent("<!DOCTYPE html>\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"vi\" xml:lang=\"vi\">\n" +
                        "  <head>\n" +
                        "    <meta charset=\"utf-8\" />\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width\" />\n" +
                        "    <title>Verification Email</title>\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <div style=\"background-color: rgb(226, 223, 223); width: 100%;height: 100%;\">\n" +
                        "        <center>\n" +
                        "            <img src=\"https://res.cloudinary.com/duk2lo18t/image/upload/v1665719834/frontend/S-Phone_cpfelx.png\" width=\"200\" height=\"200\" style=\"margin: 1%;border-radius: 1rem;\"/>\n" +
                        "            <div>\n" +
                        "                <div style=\"width: 700px; border-radius: 2%; background-color: rgb(250, 250, 250); padding: 10px;margin: 10px;\">\n" +
                        "                    <h1>Reset Password</h1>\n" +
                        "                    <h4>Hello. We, S-Phone customer service have received your confirm email request. Click the link below to confirm your email.</h4>\n" +
                        "                    <a href=\"https://phone-s.herokuapp.com/user/confirm/email?token="+jwtUtils.generateVerificationEmailJwtToken(email,user.getId())+"\"><div style=\"width: 150px; height: 50px; border-radius: 1rem; background-color: rgb(98, 130, 219); padding: 10px;margin: 10px;\">\n" +
                        "                        <p>Verification</p>\n" +
                        "                    </div>\n" +
                        "                    \n" +
                        "                    </a>\n" +
                        "                </div>\n" +
                        "                <h5>If not you? Please contact us.</h4>\n" +
                        "                <h5>Tel: 0868704516 - Email: phone.s.shop.2412@gmail.com</h4>\n" +
                        "                <br/>\n" +
                        "            </div>\n" +
                        "        </center>\n" +
                        "        \n" +
                        "    </div>\n" +
                        "  </body>\n" +
                        "</html>"
                , "text/html");
        msg.setSentDate(new Date());
        Transport.send(msg);
    }

}

