package project.phoneshop.controller;

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
import java.util.*;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    public static final String SUCCESS_URL = "/api/order/pay/success";
    public static final String CANCEL_URL = "/api/order/pay/cancel";

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final ShipService shipService;
    private final PaymentService paymentService;
    private final AddressService addressService;
    private final VoucherService voucherService;
    private final ProductService productService;
//    private final PaypalService paypalService;

    private final DiscountProgramService discountProgramService;

    @Autowired
    AuthorizationHeader authorizationHeader;

    @Autowired
    OrderMapping orderMapping;

    private static final Logger LOGGER = LogManager.getLogger(AddressController.class);
    @PostMapping("/user/order/insert")
    private ResponseEntity<SuccessResponse> insertOrder(HttpServletRequest request, @RequestBody AddNewOrderRequest addNewOrderRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            Double total = 0.0;
            List<CartEntity> listCart = new ArrayList<>();
            for(CartEntity cart: user.getListCart()){
                if(addNewOrderRequest.getListCart().contains(cart.getId())&&cart.getStatus()){
                    listCart.add(cart);
                    CartResponseFE cartResponseFE = cartService.getCartResponseFE(cart);
                    total += cartResponseFE.getPrice();
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
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Add Order Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/admin/order")
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
    @GetMapping("/user/order")
    public ResponseEntity<SuccessResponse> getOrderByUser(HttpServletRequest request) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("List Order", user.getListOrder());
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "List Order", data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/admin/order/{id}")
    public ResponseEntity<SuccessResponse> getOrderById(HttpServletRequest request,@PathVariable int id) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null) {
            Map<String, Object> data = new HashMap<>();
            OrderEntity order = orderService.findById(id);
            List<CartResponseFE> cartResponseFEList = new ArrayList<>();
            for(CartEntity cart: order.getCartOrder())
                cartResponseFEList.add(cartService.getCartResponseFE(cart));
            data.put("Order",orderService.getOrderResponse(order,cartResponseFEList));
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Order", data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
//    @GetMapping("/pay/success/{id}")
//    @ResponseBody
//    public ResponseEntity<SuccessResponse> paypalSuccess(@PathVariable("id") int id,@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId)
//    {
//        try {
//            Payment payment = paypalService.executePayment(paymentId, payerId);
//            System.out.println(payment.toJSON());
//            if (payment.getState().equals("approved")) {
//                SuccessResponse response = new SuccessResponse();
//                response.setSuccess(true);
//                response.setStatus(HttpStatus.OK.value());
//                response.setMessage("Thanh toán thành công");
//                response.getData().put("Order",orderService.findById(id));
//                return new ResponseEntity<>(response,HttpStatus.OK);
//            }
//        } catch (PayPalRESTException e) {
//            System.out.println(e.getMessage());
//        }
//        orderService.delete(id);
//        SuccessResponse response = new SuccessResponse();
//        response.setSuccess(false);
//        response.setStatus(HttpStatus.FAILED_DEPENDENCY.value());
//        response.setMessage("Thanh toán thất bại");
//        return new ResponseEntity<>(response,HttpStatus.FAILED_DEPENDENCY);
//    }
//
//    @GetMapping("/pay/cancel/{id}")
//    public ResponseEntity<SuccessResponse> paypalCancel(@PathVariable("id") int id) {
//        orderService.delete(id);
//        SuccessResponse response = new SuccessResponse();
//        response.setSuccess(false);
//        response.setStatus(HttpStatus.FAILED_DEPENDENCY.value());
//        response.setMessage("Thanh toán thất bại (cancel)");
//        return new ResponseEntity<>(response,HttpStatus.FAILED_DEPENDENCY);
//    }
//    private ResponseEntity SendErrorValid(String field, String message,String title){
//        ErrorResponseMap errorResponseMap = new ErrorResponseMap();
//        Map<String,String> temp =new HashMap<>();
//        errorResponseMap.setMessage(title);
//        temp.put(field,message);
//        errorResponseMap.setStatus(HttpStatus.BAD_REQUEST.value());
//        errorResponseMap.setDetails(temp);
//        return ResponseEntity
//                .badRequest()
//                .body(errorResponseMap);
//    }
//
//    private double PayMoney(CartEntity cart,ShipEntity ship, VoucherEntity voucher,AddOrderRequest request) {
//        double totalOrderProduct = PriceProduct(cart,request);
//
//        double totalOrder = 0.0;
//        double voucherValue=0.0;
//
//
//        Date today = new Date();
//
//        if(voucher==null){
//            voucherValue = 0.0;
//        }
//        else if(voucher.getFromDate().compareTo(today)<0 && voucher.getToDate().compareTo(today)>0){
//            if (voucher.getValue().contains("%")) {
//                double value = Double.parseDouble(voucher.getValue().substring(0,voucher.getValue().length()-1));
//                voucherValue = totalOrderProduct * value / 100;
//            }else {
//                voucherValue = Double.parseDouble(voucher.getValue());
//            }
//        }
//        totalOrder = totalOrderProduct + ship.getShipPrice() - voucherValue;
//        return totalOrder;
//    }
//
//    private void processCartItem(AddOrderRequest request, CartEntity cart, UserEntity user){
//        List<CartItemEntity> listPick = new ArrayList<CartItemEntity>();
//        List<CartItemEntity> listLeft = new ArrayList<CartItemEntity>();
//
//
//        for (int i : request.getCartItem())
//        {
//            CartItemEntity cartItem =cartService.getItemByIdAndCart(i,cart);
//            listPick.add(cartItem);
//            cart.getCartItem().remove(cartItem);
//        }
//
//        listLeft = cart.getCartItem();
//        cart.setCartItem(listPick);
//        cart.setStatus(false);
//        CartEntity newCart = new CartEntity(0.0,true,user);
//        newCart.setCartItem(listLeft);
//        for(CartItemEntity cartItem : listLeft)
//        {
//            cartItem.setCart(newCart);
////            cartService.saveCartItem(cartItem);
//        }
//        cartService.saveCart(newCart);
//    }
//    private String paypalPayment(OrderEntity order)
//    {
//        try {
//            orderService.save(order);
//            Payment paypalPayment = paypalService.createPayment(order, "USD", "paypal",
//                    "sale", "https://nhom3-tiki.herokuapp.com" + CANCEL_URL+"/"+order.getOrderId(),
//                    "https://nhom3-tiki.herokuapp.com" + SUCCESS_URL +"/"+order.getOrderId());
//            for (Links link : paypalPayment.getLinks()) {
//                if (link.getRel().equals("approval_url")) {
//                    return link.getHref();
//                }
//            }
//        }
//        catch (PayPalRESTException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    private double PriceProduct(CartEntity cart,AddOrderRequest request){
//        double totalOrderProduct = 0.0;
//        Date today = new Date();
//
//        for (CartItemEntity item : cart.getCartItem()) {
//            DiscountProgramEntity discountProgram = discountProgramService.findByIdAndProductBrand(request.getDiscountId(), item.getProduct().getProductBrand());
//            if(request.getDiscountId()==null){
//                totalOrderProduct += item.getProduct().getPrice() * item.getQuantity();
//                item.getProduct().setInventory(item.getProduct().getInventory()-item.getQuantity());
//                item.getProduct().setSellAmount(item.getQuantity()+ item.getProduct().getSellAmount());
//            }else if (discountProgram != null && discountProgram.getFromDate().compareTo(today) < 0 && discountProgram.getToDate().compareTo(today) > 0) {
//                double discount = item.getProduct().getPrice() * discountProgram.getPercent() / 100;
//                totalOrderProduct += (item.getProduct().getPrice() - discount) * item.getQuantity();
//                item.getProduct().setInventory(item.getProduct().getInventory() - item.getQuantity());
//                item.getProduct().setSellAmount(item.getQuantity() + item.getProduct().getSellAmount());
//            }
//        }
//        return totalOrderProduct;
//    }
    private List<String> listOrderSort(){
        List<String> list = new ArrayList<>();
        list.add("order_id");
        list.add("created_date");
        list.add("total");
        return list;
    }
}
