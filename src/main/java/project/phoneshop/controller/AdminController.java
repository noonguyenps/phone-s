package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.mapping.UserNotificationMapping;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.entity.UserNotificationEntity;
import project.phoneshop.model.payload.request.notification.AddNotificationRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.service.OrderService;
import project.phoneshop.service.ProductService;
import project.phoneshop.service.UserNotificationService;
import project.phoneshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final UserNotificationService userNotificationService;
    private final ProductService productService;
    private final OrderService orderService;
    @Autowired
    UserNotificationMapping userNotificationMapping;
    @Autowired
    AuthorizationHeader authorizationHeader;
    @GetMapping("/user/all")
    public ResponseEntity<SuccessResponse> getAllUser(HttpServletRequest request, @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "20")int size){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<UserEntity> list = userService.getAllUser(page,size);
            Map<String,Object> data = new HashMap<>();
            data.put("listUser",list);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List User",data),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<SuccessResponse> getUserByID(HttpServletRequest request,@PathVariable UUID id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            UserEntity userEntity = userService.findById(id);
            if(userEntity == null)
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_FOUND.value(), "user not found",null),HttpStatus.NOT_FOUND);
            Map<String,Object> data = new HashMap<>();
            data.put("user",userEntity);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"User",data),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/list")
    public ResponseEntity<SuccessResponse> getAllNotification(HttpServletRequest request){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<UserNotificationEntity> list = userNotificationService.getAll();
            Map<String,Object> data = new HashMap<>();
            data.put("listNotifications",list);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List Notifications",data),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/notification/create")
    public ResponseEntity<SuccessResponse> createNotification(HttpServletRequest request,@RequestBody AddNotificationRequest addNotificationRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            UserNotificationEntity notification = userNotificationMapping.modelToEntity(addNotificationRequest);
            try {
                userNotificationService.create(notification,user);
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Create notification successfully",null),HttpStatus.OK);
            } catch (Exception e){
                e.printStackTrace();
            }
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Create notification failure",null),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("statistic")
    public ResponseEntity<SuccessResponse> getStatistic(HttpServletRequest request){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            long countUser = userService.getCountUser();
            long countProduct = productService.countProduct();
            long countOrder = orderService.countOrder();
            long countOrderPrice = orderService.countOrderPrice();
            Map<String,Object> data = new HashMap<>();
            data.put("countUser",countUser);
            data.put("countProduct",countProduct);
            data.put("countOrder",countOrder);
            data.put("countOrderPrice",countOrderPrice);
            Double[] statisticMonth = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            List<OrderEntity> orderEntities = orderService.getAll();
            for(OrderEntity order: orderEntities){
                statisticMonth[order.getCreatedDate().getMonth()] += order.getTotal();
            }
            data.put("statisticMonth",statisticMonth);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Statistic",data),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
