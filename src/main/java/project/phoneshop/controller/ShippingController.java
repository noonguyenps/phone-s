package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.phoneshop.mapping.ShipMapping;
import project.phoneshop.mapping.ShippingMapping;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ShipEntity;
import project.phoneshop.model.entity.ShippingEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.request.ship.AddShipRequest;
import project.phoneshop.model.payload.request.shipping.AddShippingRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.model.payload.response.shipping.ShippingResponse;
import project.phoneshop.service.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ShippingController {
    private final ShippingService shippingService;
    private final ImageStorageService imageStorageService;
    private final OrderService orderService;
    private final ShippingMapping shippingMapping;
    private final UserService userService;
    @GetMapping("admin/shipping/list")
    public ResponseEntity<SuccessResponse> getAllShipping(){
        List<ShippingEntity> list = shippingService.getAllShipping();
        Map<String, Object> data = new HashMap<>();
        List<ShippingResponse> shippingResponses = new ArrayList<>();
        for (ShippingEntity shipping: list)
            shippingResponses.add(shippingService.entity2Response(shipping));
        data.put("listShipping",shippingResponses);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List shipping",data),HttpStatus.OK);
    }

    @GetMapping("shipping/{id}")
    public ResponseEntity<SuccessResponse> getShippingById(@PathVariable("id")int id, @RequestParam String secretKey){
        OrderEntity orderEntity = orderService.findShipping(id,secretKey);
        if(orderEntity==null||orderEntity.getOrderStatus()!=1){
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Order Not Found",null),HttpStatus.NOT_FOUND);
        }
        else {
            ShippingEntity shipping = shippingService.getInfoShippingByOrderId(orderEntity);
            Map<String,Object> data = new HashMap<>();
            data.put("shipping",shippingService.entity2Response(shipping));
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "shipping",data),HttpStatus.OK);
        }
    }

    @PostMapping("/manager/shipping/create")
    public ResponseEntity<SuccessResponse> createShip(@RequestBody AddShippingRequest request){
        OrderEntity order= orderService.findById(request.getOrder());
        if(order==null){
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Order not found",null),HttpStatus.NOT_FOUND);
        }
        UserEntity user = order.getUserOrder();
        if(user==null)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "User not Found",null),HttpStatus.NOT_FOUND);
        ShippingEntity shipping = shippingMapping.requestToEntity(request,order,user);
        shippingService.create(shipping);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "Insert successfully",null),HttpStatus.OK);
    }

    @PutMapping("/shipping/update/{id}")
    public ResponseEntity<SuccessResponse> updateShipType(@PathVariable("id") int id,@RequestParam String secretKey,@RequestParam String img1,@RequestParam(required = false) String img2,@RequestParam(required = false) String img3){
        OrderEntity orderEntity = orderService.findShipping(id,secretKey);
        if(orderEntity==null||orderEntity.getOrderStatus()!=1){
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Order Not Found",null),HttpStatus.NOT_FOUND);
        }
        else {
            ShippingEntity shipping = shippingService.getInfoShippingByOrderId(orderEntity);
            shipping.setImage1(img1);
            shipping.setImage2(img2);
            shipping.setImage3(img3);
            shipping.setState(2);
            shippingService.create(shipping);
            orderEntity.setStatusPayment(true);
            orderEntity.setOrderStatus(2);
            orderService.save(orderEntity);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "Update Image Successfully",null),HttpStatus.OK);
        }
    }
    @PostMapping(value = "/shipping/uploadImg/{id}")
    public ResponseEntity<SuccessResponse> uploadImgShipping(@PathVariable("id") int id,@RequestParam String secretKey, @RequestPart(required = true) MultipartFile file){
//        if(!imageStorageService.isImageFile(file))
//            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),"The file is not an image",null), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        UUID uuid = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        String url = imageStorageService.saveShippingImg(file, date.toString()+"/"+String.valueOf(uuid));
        if(url.equals(""))
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Upload Image Failure",null), HttpStatus.NOT_FOUND);
        Map<String, Object> data = new HashMap<>();
        data.put("url",url);
        return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Upload Logo Successfully",data), HttpStatus.OK);
    }

}
