package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.mapping.UserMapping;
import project.phoneshop.mapping.UserNotificationMapping;
import project.phoneshop.model.entity.*;
import project.phoneshop.model.payload.request.notification.AddNotificationRequest;
import project.phoneshop.model.payload.request.user.AddNewUserRequest;
import project.phoneshop.model.payload.response.CountPerMonth;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.model.payload.response.cart.CartResponseFE;
import project.phoneshop.model.payload.response.order.OrderResponse;
import project.phoneshop.model.payload.response.role.RoleResponse;
import project.phoneshop.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigInteger;
import java.time.LocalDate;
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
    private final CartService cartService;
    private final RoleService roleService;
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
    @GetMapping("/manager/all")
    public ResponseEntity<SuccessResponse> getAllManager(HttpServletRequest request, @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "20")int size){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<UserEntity> list = userService.getAllManager(page,size);
            Map<String,Object> data = new HashMap<>();
            data.put("listManager",list);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List Manager",data),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/shipper/all")
    public ResponseEntity<SuccessResponse> getAllShipper(HttpServletRequest request, @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "20")int size){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<UserEntity> list = userService.getAllShipper(page,size);
            Map<String,Object> data = new HashMap<>();
            data.put("listShipper",list);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List Shipper",data),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/user/address/{id}")
    @ResponseBody
    public ResponseEntity<SuccessResponse> getUserAddress(HttpServletRequest request, @PathVariable UUID id) throws Exception{
        UserEntity user = userService.findById(id);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            List<AddressEntity> list = user.getAddress();
            if (list.isEmpty())
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List address is Empty",null), HttpStatus.OK);
            Map<String,Object> data = new HashMap<>();
            data.put("addressList", list);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List address",data), HttpStatus.OK);
        }
    }
    @GetMapping("/user/order/{id}")
    @ResponseBody
    public ResponseEntity<SuccessResponse> getUserOrders(HttpServletRequest request, @PathVariable UUID id) throws Exception{
        UserEntity user = userService.findById(id);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            List<OrderEntity> list = user.getListOrder();
            List<OrderResponse> responseList = new ArrayList<>();
            if (list.isEmpty())
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List address is Empty",null), HttpStatus.OK);
            for(OrderEntity order:list){
                List<CartResponseFE> cartResponseFEList = new ArrayList<>();
                for(CartEntity cart: order.getCartOrder())
                    cartResponseFEList.add(cartService.getCartResponseFE(cart));
                responseList.add(orderService.getOrderResponse(order,cartResponseFEList));
            }
            Map<String,Object> data = new HashMap<>();
            data.put("listOrder",responseList);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List Order",data), HttpStatus.OK);
        }
    }
    @GetMapping("/pdf/order/{id}")
    @ResponseBody
    public ResponseEntity<SuccessResponse> getPDFByOrder(HttpServletRequest request, @PathVariable UUID id) throws Exception{
        UserEntity user = userService.findById(id);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            List<OrderEntity> list = user.getListOrder();
            List<OrderResponse> responseList = new ArrayList<>();
            if (list.isEmpty())
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List address is Empty",null), HttpStatus.OK);
            for(OrderEntity order:list){
                List<CartResponseFE> cartResponseFEList = new ArrayList<>();
                for(CartEntity cart: order.getCartOrder())
                    cartResponseFEList.add(cartService.getCartResponseFE(cart));
                responseList.add(orderService.getOrderResponse(order,cartResponseFEList));
            }
            Map<String,Object> data = new HashMap<>();
            data.put("listOrder",responseList);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List Order",data), HttpStatus.OK);
        }
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<SuccessResponse> getUserByID(HttpServletRequest request,@PathVariable UUID id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            UserEntity userEntity = userService.findById(id);
            if(userEntity == null)
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_FOUND.value(), "user not found",null),HttpStatus.NOT_FOUND);
            Map<String,Object> data = new HashMap<>();
            data.put("user",userService.getUserResponseAdmin(userEntity));
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"User",data),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/role/all")
    public ResponseEntity<SuccessResponse> getAllRole(HttpServletRequest request){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            Map<String,Object> data = new HashMap<>();
            List<RoleEntity> roleEntities = roleService.getAllRoles();
            List<RoleResponse> responseList = new ArrayList<>();
            for (RoleEntity role: roleEntities){
                responseList.add(roleService.getRoleResponse(role));
            }
            data.put("roles",responseList);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Roles",data),HttpStatus.OK);
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
    @PostMapping("/manager/create")
    public ResponseEntity<SuccessResponse> createManagerAccount(@RequestBody @Valid AddNewUserRequest request) {
        UserEntity user= UserMapping.registerToEntity(request);
        if(userService.existsByPhone(user.getPhone()))
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.CONFLICT.value(),"Create Failure! Phone number is exist",null), HttpStatus.CONFLICT);;
        try{
            userService.saveUser(user,"MANAGER");
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Create Successfully",null), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_ACCEPTABLE.value(),"Create Failure",null), HttpStatus.NOT_ACCEPTABLE);
    }
    @PostMapping("/shipper/create")
    public ResponseEntity<SuccessResponse> createShipperAccount(@RequestBody @Valid AddNewUserRequest request) {
        UserEntity user= UserMapping.registerToEntity(request);
        if(userService.existsByPhone(user.getPhone()))
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.CONFLICT.value(),"Create Failure! Phone number is exist",null), HttpStatus.CONFLICT);;
        try{
            userService.saveUser(user,"SHIPPER");
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Create Successfully",null), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_ACCEPTABLE.value(),"Create Failure",null), HttpStatus.NOT_ACCEPTABLE);
    }
    @GetMapping("statistic")
    public ResponseEntity<SuccessResponse> getStatistic(HttpServletRequest request){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            long countProduct = productService.countProduct();
            int totalUser = 0;
            int totalOrder = 0;
            int totalRevenueOrder = 0;
            Map<String,Object> data = new HashMap<>();
            List<Object> countUserPerMonth = userService.countUserPerMonth();
            int orderPerMonth[] = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0} ;
            int userPerMonth[] = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0} ;
            for(Object countUser : countUserPerMonth){
                totalUser += Integer.valueOf(((Object[])countUser)[2].toString());
                if(String.valueOf(((Object[])countUser)[1]).equals(String.valueOf(LocalDate.now().getYear()))){
                    int i = Integer.parseInt(((Object[]) countUser)[0].toString()) - 1;
                    userPerMonth[i] = Integer.parseInt(((Object[])countUser)[2].toString());
                }
            }
            List<Object> countOrderPerMonth = orderService.countUserPerMonth();
            for(Object countOrder : countOrderPerMonth){
                totalOrder += Integer.valueOf(((Object[])countOrder)[2].toString());
                double revenueOrderTemp = Double.valueOf(((Object[])countOrder)[3].toString());
                totalRevenueOrder += (int)Math.round(revenueOrderTemp);
                if(String.valueOf(((Object[])countOrder)[1]).equals(String.valueOf(LocalDate.now().getYear()))){
                    int i = Integer.parseInt(((Object[]) countOrder)[0].toString()) - 1;
                    orderPerMonth[i] = Integer.parseInt(((Object[])countOrder)[2].toString());
                }
            }
            List<Object> categoryPrice = productService.getTotalByCategory();
            List<Map<String,Object>> listCategoryPrice = new ArrayList<>();
            for(Object a : categoryPrice){
                boolean stop = false;
                for(Map<String,Object> b: listCategoryPrice){
                    if(b.get("name").equals(((Object[])a)[0].toString())){
                        double temp = (double) b.get("value");
                        b.put("value",temp + Double.parseDouble(((Object[])a)[1].toString()));
                        stop = true;
                        break;
                    }
                }
                if(!stop){
                    Map<String,Object> map = new HashMap<>();
                    map.put("name",((Object[])a)[0].toString());
                    map.put("value",Double.parseDouble(((Object[])a)[1].toString()));
                    listCategoryPrice.add(map);
                }
            }

            double revenue = orderService.countOrderPrice();
            data.put("categoryPrice",listCategoryPrice);
            data.put("userPerMonth",userPerMonth);
            data.put("orderPerMonth",orderPerMonth);
            data.put("countProducts",countProduct);
            data.put("countUser",totalUser);
            data.put("countOrder",totalOrder);
            data.put("countRevenue",revenue);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Statistic",data),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
