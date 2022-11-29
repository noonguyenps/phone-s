package project.phoneshop.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.mapping.UserNotificationMapping;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.entity.UserNotificationEntity;
import project.phoneshop.model.payload.request.notification.AddNotificationRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.service.UserNotificationService;
import project.phoneshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserNotificationController {

    private final UserNotificationService userNotificationService;
    @Autowired
    AuthorizationHeader authorizationHeader;
    @GetMapping("user/notification")
    public ResponseEntity<SuccessResponse> getUserNotification(HttpServletRequest request) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<UserNotificationEntity> userListNotification = userNotificationService.findNotificationByUser(user);
            if (userListNotification.isEmpty())
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_FOUND.value(),"List Notifications is Empty",null),HttpStatus.NOT_FOUND);
            Map<String, Object> data = new HashMap<>();
            data.put("listNotification",userListNotification);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List Notifications",data),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @DeleteMapping("user/notification/id")
    public ResponseEntity<SuccessResponse> deleteNotification(HttpServletRequest request,@PathVariable("id")int id) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            try {
                userNotificationService.delete(id);
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Notification was deleted",null),HttpStatus.OK);
            }catch (Exception e){
                throw new Exception(e.getMessage() + "\nDelete notification fail");
            }
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
