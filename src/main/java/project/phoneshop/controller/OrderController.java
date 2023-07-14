package project.phoneshop.controller;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.mapping.OrderMapping;
import project.phoneshop.mapping.ProductMapping;
import project.phoneshop.model.entity.*;
import project.phoneshop.model.payload.request.order.AddNewOrderRequest;
import project.phoneshop.model.payload.request.order.AddOrderRequest;
import project.phoneshop.model.payload.request.product.AddNewProductRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.model.payload.response.cart.CartResponseFE;
import project.phoneshop.model.payload.response.order.OrderResponse;
import project.phoneshop.model.payload.response.product.ProductResponse;
import project.phoneshop.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final ShipService shipService;
    private final PaymentService paymentService;
    private final PaypalService paypalService;
    private final MomoService momoService;
    private final AddressService addressService;
    private final VoucherService voucherService;
    private final ProductService productService;
    private final UserNotificationService userNotificationService;
    public static final String SUCCESS_URL = "/api/order/pay/success";
    public static final String CANCEL_URL = "/api/order/pay/cancel";

    @Autowired
    AuthorizationHeader authorizationHeader;

    @Autowired
    OrderMapping orderMapping;

    private static final Logger LOGGER = LogManager.getLogger(AddressController.class);
    @PostMapping("/user/order/insert")
    private ResponseEntity<SuccessResponse> insertOrder(HttpServletRequest request, @RequestBody AddNewOrderRequest addNewOrderRequest) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            Double total = 0.0;
            List<CartEntity> listCart = new ArrayList<>();
            for(CartEntity cart: user.getListCart()){
                if(addNewOrderRequest.getListCart().contains(cart.getId())&&cart.getStatus()){
                    listCart.add(cart);
                    CartResponseFE cartResponseFE = cartService.getCartResponseFE(cart);
                    total += cartResponseFE.getPrice()*cartResponseFE.getQuantity();
                }
            }
            if(listCart.isEmpty()){
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "No Cart in order",null),HttpStatus.NOT_FOUND);
            }
            AddressEntity address = null;
            for(AddressEntity addressEntity : user.getListAddress()){
                if(addressEntity.getId().equals(addNewOrderRequest.getAddress())){
                    address = addressEntity;
                }
            }
            if(address == null){
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Address in order not found",null),HttpStatus.NOT_FOUND);
            }
            PaymentEntity payment = paymentService.getPaymentById(addNewOrderRequest.getPayment());
            if(payment==null) return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Payment in order Not Found",null),HttpStatus.NOT_FOUND);
            ShipEntity ship = shipService.findShipById(addNewOrderRequest.getShip());
            if(ship == null) return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Ship in order is not found",null),HttpStatus.NOT_FOUND);
            VoucherEntity voucher = null;
            if(!addNewOrderRequest.isNullVoucher()){
                VoucherEntity voucherTemp = voucherService.findById(addNewOrderRequest.getVoucher());
                if(voucherTemp==null){
                    return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Voucher not Found",null),HttpStatus.NOT_FOUND);
                }
                for(UserEntity user1: voucherTemp.getUserEntities()){
                    if(user1.getId().equals(user.getId())){
                        voucher = voucherTemp;
                    }
                }
                if(voucher== null) return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Voucher in order is not found",null),HttpStatus.NOT_FOUND);
            }
            OrderEntity order = OrderMapping.addOrderToEntity(user,listCart,address,payment,ship,voucher,total);
            String generatedString = RandomStringUtils.random(20, true, false);
            order.setName(generatedString);
            order.setCartOrder(listCart);
            orderService.save(order);
            OrderEntity order1 = orderService.findOrderByName(generatedString);
            for(CartEntity cart: listCart){
                cart.setActive(false);
                cart.setOrder(order1);
                ProductEntity product = cart.getProductCart();
                product.setInventory(product.getInventory()-cart.getQuantity());
                productService.saveProduct(product);
                cartService.saveCart(cart);
            }
            HashMap<String,Object> data=new HashMap<>();
            String link = "";
            switch (payment.getPaymentId()){
                case 2 : link=paypalService.paypalPayment(order1,request);break;
                case 3 : link= momoService.createMomoPayment(order1);break;
                case 4 : link= momoService.createMomoATMPayment(order1);break;
            }
            data.put("url",link);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Add Order Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/user/order/payment/{id}")
    private ResponseEntity<SuccessResponse> paymentOrder(HttpServletRequest request, @PathVariable int id, @RequestParam(defaultValue = "1") int paymentId) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        OrderEntity order = orderService.findById(id);
        if(user != null){
            if(order==null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Order not found",null),HttpStatus.NOT_FOUND);
            if(order.getUserOrder()!=user)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Order not found",null),HttpStatus.NOT_FOUND);
            if(order.getStatusPayment())
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Order was paid",null),HttpStatus.NOT_FOUND);
            PaymentEntity payment = paymentService.getPaymentById(paymentId);
            if(payment==null) return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Payment in order Not Found",null),HttpStatus.NOT_FOUND);
            HashMap<String,Object> data=new HashMap<>();
            String link = "";
            switch (payment.getPaymentId()){
                case 2 : link=paypalService.paypalPayment(order,request);break;
                case 3 : link= momoService.createMomoPayment(order);break;
                case 4 : link= momoService.createMomoATMPayment(order);break;
            }
            data.put("url",link);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Payment Order Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/order/momo/pay")
    public ResponseEntity<Object> payMomoResult(@RequestParam("orderId") String orderId,
                                                @RequestParam("requestId") String requestId,
                                                HttpServletResponse response){
        try{
            int orderId1 = Integer.parseInt(orderId) - 4720000;
            OrderEntity orderEntity = orderService.findById(orderId1);
            if(orderEntity==null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Order not found",null), HttpStatus.NOT_FOUND);
            int resultCode = momoService.getResultCode(Integer.parseInt(orderId),requestId);
            if(resultCode==0){
                orderEntity.setStatusPayment(true);
                PaymentEntity payment = paymentService.getPaymentById(4);
                orderEntity.setPaymentOrder(payment);
                orderService.save(orderEntity);
            }
            response.sendRedirect("https://phone-s-fe.vercel.app/payment/"+orderId1);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Add Order Successfully",null), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),e.getMessage(),null), HttpStatus.OK);
        }
    }
    @GetMapping("/user/order/pay/status/{id}")
    public ResponseEntity<SuccessResponse> checkPaymentStatus(@PathVariable("id") String id,
                                                              HttpServletRequest request){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            OrderEntity order = orderService.findById(Integer.parseInt(id));
            if(order==null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Order not found",null),HttpStatus.NOT_FOUND);
            if(order.getUserOrder()!=user)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Order not found",null),HttpStatus.NOT_FOUND);
            if(order.getStatusPayment()==true)
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Successfully",null), HttpStatus.OK);
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_ACCEPTABLE.value(),"Failure",null), HttpStatus.NOT_ACCEPTABLE);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/order/pay/success/{id}")
    public ResponseEntity<Object> successPay(@PathVariable("id") String id,
                                             @RequestParam("paymentId") String paymentId,
                                             @RequestParam("redirectURI") String uri,
                                             @RequestParam("PayerID") String payerId,
                                             HttpServletResponse response){
        //Execute payment
        try{
            Payment payment=paypalService.executePayment(paymentId,payerId);
            if(payment.getState().equals("approved")){
                Map<String,Object> data=new HashMap<>();
                OrderEntity order = orderService.findById(Integer.parseInt(id));
                PaymentEntity paymentEntity = paymentService.getPaymentById(2);
                order.setPaymentOrder(paymentEntity);
                order.setStatusPayment(true);
                orderService.save(order);
                //Process order if payment success
                data.put("orderId",id);
                response.sendRedirect("https://phone-s-fe.vercel.app/payment/"+id);
                return new ResponseEntity(new SuccessResponse(true,HttpStatus.OK.value(),"Payment success",data),HttpStatus.OK);
            }
        }
        catch (PayPalRESTException e){
            return new ResponseEntity(new SuccessResponse(true,HttpStatus.BAD_REQUEST.value(),"Payment failure",null),HttpStatus.BAD_REQUEST);
        }
        catch (IOException e1){
            return new ResponseEntity(new SuccessResponse(true,HttpStatus.BAD_REQUEST.value(),"Payment failure",null),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new SuccessResponse(true,HttpStatus.BAD_REQUEST.value(),"Payment failure",null),HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/order/momo/pay/failure/{id}")
    public ResponseEntity<Object> failureMomoPay(@PathVariable("id") String id){
        return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.OK.value(),"a",null),HttpStatus.OK);
    }
    @GetMapping("/order/pay/cancel/{id}")
    public ResponseEntity<Object> cancelPay(@PathVariable String id, HttpServletResponse response) throws IOException {
        orderService.changePaymentStatus(Integer.parseInt(id),false);
        response.sendRedirect("https://phone-s-fe.vercel.app/payment/"+id);
        return new ResponseEntity(new SuccessResponse(true,HttpStatus.BAD_REQUEST.value(),"Payment failure",null),HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/manager/order")
    public ResponseEntity<SuccessResponse> getAllOrder(HttpServletRequest request,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "order_id") String sort){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if(!listOrderSort().contains(sort))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
            List<OrderEntity> listOrder = orderService.findAllOrder(page, size, sort);
            if(listOrder.size() == 0)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"List Order is Empty",null), HttpStatus.FOUND);
            Map<String, Object> data = new HashMap<>();
            List<OrderResponse> orderResponseList = new ArrayList<>();
            for(OrderEntity order:listOrder){
                List<CartResponseFE> cartResponseFEList = new ArrayList<>();
                for(CartEntity cart: order.getCartOrder())
                    cartResponseFEList.add(cartService.getCartResponseFE(cart));
                orderResponseList.add(orderService.getOrderResponse(order,cartResponseFEList));
            }
            data.put("listOrder",orderResponseList);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "List Order",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/manager/order/status")
    public ResponseEntity<SuccessResponse> getAllOrderByStatus(HttpServletRequest request,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "0") int status){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<OrderEntity> listOrder = orderService.findAllOrderByStatus(status, page, size);
            if(listOrder.size() == 0)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"List Order is Empty",null), HttpStatus.FOUND);
            Map<String, Object> data = new HashMap<>();
            List<OrderResponse> orderResponseList = new ArrayList<>();
            for(OrderEntity order:listOrder){
                List<CartResponseFE> cartResponseFEList = new ArrayList<>();
                for(CartEntity cart: order.getCartOrder())
                    cartResponseFEList.add(cartService.getCartResponseFE(cart));
                orderResponseList.add(orderService.getOrderResponse(order,cartResponseFEList));
            }
            data.put("listOrder",orderResponseList);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "List Order",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/user/order")
    public ResponseEntity<SuccessResponse> getOrderByUser(HttpServletRequest request,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "order_id") String sort) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null) {
            Map<String, Object> data = new HashMap<>();
            List<OrderResponse> orderResponseList = new ArrayList<>();
            if(!listOrderSort().contains(sort))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Sort not found",null),HttpStatus.NOT_FOUND);
            switch (sort){
                case "total_down":user.getListOrder().sort(Comparator.comparing(OrderEntity::getTotal).reversed());break;
                case "total_up": user.getListOrder().sort(Comparator.comparing(OrderEntity::getTotal));break;
                case "created_date": user.getListOrder().sort(Comparator.comparing(OrderEntity::getCreatedDate));break;
                case "order_id": user.getListOrder().sort(Comparator.comparing(OrderEntity::getOrderId).reversed());break;
            }
            user.getListOrder().sort(Comparator.comparing(OrderEntity::getOrderId));
            int i = 0;
            for(OrderEntity order:user.getListOrder()){
                if(i>page*size&&i<(page+1)*size){
                    List<CartResponseFE> cartResponseFEList = new ArrayList<>();
                    for(CartEntity cart: order.getCartOrder())
                        cartResponseFEList.add(cartService.getCartResponseFE(cart));
                    orderResponseList.add(orderService.getOrderResponse(order,cartResponseFEList));
                }
                i++;
                if(orderResponseList.size()==size)
                    break;
            }
            data.put("listOrder",orderResponseList);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "List Order", data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/user/order/status")
    public ResponseEntity<SuccessResponse> getOrderByUserAndStatus(HttpServletRequest request, int status) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null) {
            Map<String, Object> data = new HashMap<>();
            List<OrderResponse> orderResponseList = new ArrayList<>();
            for(OrderEntity order:user.getListOrder()){
                if(order.getOrderStatus()==status) {
                    List<CartResponseFE> cartResponseFEList = new ArrayList<>();
                    for(CartEntity cart: order.getCartOrder())
                        cartResponseFEList.add(cartService.getCartResponseFE(cart));
                    orderResponseList.add(orderService.getOrderResponse(order,cartResponseFEList));
                }
            }
            data.put("listOrder",orderResponseList);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "List Order", data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/manager/order/{id}")
    public ResponseEntity<SuccessResponse> getOrderById(HttpServletRequest request,@PathVariable int id) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null) {
            Map<String, Object> data = new HashMap<>();
            OrderEntity order = orderService.findById(id);
            List<CartResponseFE> cartResponseFEList = new ArrayList<>();
            for(CartEntity cart: order.getCartOrder())
                cartResponseFEList.add(cartService.getCartResponseFE(cart));
            data.put("Order",orderService.getOrderAdminResponse(order,cartResponseFEList));
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Order", data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/user/order/{id}")
    public ResponseEntity<SuccessResponse> getOrderInUserById(HttpServletRequest request,@PathVariable int id) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null) {
            Map<String, Object> data = new HashMap<>();
            OrderEntity order = orderService.findById(id);
            if(order.getUserOrder()!=user){
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED",null),HttpStatus.UNAUTHORIZED);
            }
            List<CartResponseFE> cartResponseFEList = new ArrayList<>();
            for(CartEntity cart: order.getCartOrder())
                cartResponseFEList.add(cartService.getCartResponseFE(cart));
            data.put("order",orderService.getOrderResponse(order,cartResponseFEList));
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Order", data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @DeleteMapping("user/order/delete/{id}")
    public ResponseEntity<SuccessResponse> deleteOrderById(HttpServletRequest request, @PathVariable int id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null) {
            OrderEntity order = orderService.findById(id);
            if(order==null){
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Order not found",null),HttpStatus.NOT_FOUND);
            }
            if(order.getUserOrder()!=user){
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED",null),HttpStatus.UNAUTHORIZED);
            }
            order.setOrderStatus(3);
            orderService.save(order);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Delete order successfully", null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PutMapping("/manager/order/change/{id}")
    public ResponseEntity<SuccessResponse> changeStatusOrder(HttpServletRequest request, @PathVariable int id, @RequestParam(defaultValue = "0") int status){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null) {
            Map<String, Object> data = new HashMap<>();
            OrderEntity order = orderService.findById(id);
            if(order==null){
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Order not found",null),HttpStatus.NOT_FOUND);
            }
            order.setOrderStatus(status);
            orderService.save(order);
            String message = "";
            if(status == 1){
                UserNotificationEntity notificationEntity = new UserNotificationEntity();
                notificationEntity.setType("order");
                notificationEntity.setDateCreate(new Date());
                notificationEntity.setStatus(1);
                notificationEntity.setUser(order.getUserOrder());
                message = "Đơn hàng của bạn đang được vận chuyển";
                notificationEntity.setMessage(message);
                userNotificationService.saveNotification(notificationEntity);
            }
            else if( status == 2){
                UserNotificationEntity notificationEntity = new UserNotificationEntity();
                notificationEntity.setType("order");
                notificationEntity.setDateCreate(new Date());
                notificationEntity.setStatus(1);
                notificationEntity.setUser(order.getUserOrder());
                message = "Đơn hàng của bạn đã được giao thành công";
                notificationEntity.setMessage(message);
                userNotificationService.saveNotification(notificationEntity);
            }
            else if( status == 3){
                UserNotificationEntity notificationEntity = new UserNotificationEntity();
                notificationEntity.setType("order");
                notificationEntity.setDateCreate(new Date());
                notificationEntity.setStatus(1);
                notificationEntity.setUser(order.getUserOrder());
                message = "Đơn hàng của bạn bị hủy bởi cửa hàng";
                notificationEntity.setMessage(message);
                userNotificationService.saveNotification(notificationEntity);
            }
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Order", data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    private List<String> listOrderSort(){
        List<String> list = new ArrayList<>();
        list.add("order_id");
        list.add("created_date");
        list.add("total_up");
        list.add("total_down");
        return list;
    }
}
