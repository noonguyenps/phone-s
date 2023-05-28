package project.phoneshop.controller;

import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.mapping.ShipMapping;
import project.phoneshop.mapping.ShippingMapping;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ShipEntity;
import project.phoneshop.model.entity.ShippingEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.request.ship.AddShipRequest;
import project.phoneshop.model.payload.request.shipping.AddShippingRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.model.payload.response.shipping.ShippingPDFExporter;
import project.phoneshop.model.payload.response.shipping.ShippingResponse;
import project.phoneshop.model.payload.response.shipping.ShippingResponseV2;
import project.phoneshop.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    @Autowired
    AuthorizationHeader authorizationHeader;
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
    @GetMapping("/shipping/export/pdf/{id}")
    public void exportToPDF(HttpServletResponse response,@PathVariable UUID id) throws DocumentException, IOException, JSONException {
        ShippingEntity shipping = shippingService.findById(id);
        if(shipping!=null) {
            response.setContentType("application/pdf");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
            response.setHeader(headerKey, headerValue);
            ShippingPDFExporter exporter = new ShippingPDFExporter(shipping);
            exporter.export(response);
        }
    }
    @GetMapping("manager/shipper/all")
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
    @GetMapping("shipper/shipping/list")
    public ResponseEntity<SuccessResponse> getAllShippingByShipper(HttpServletRequest request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        List<ShippingEntity> list = shippingService.getAllShippingByShipper(user,page,size);
        Map<String, Object> data = new HashMap<>();
        List<ShippingResponseV2> shippingResponses = new ArrayList<>();
        for (ShippingEntity shipping: list)
            shippingResponses.add(shippingService.shippingResponse(shipping));
        data.put("listShipping",shippingResponses);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List shipping",data),HttpStatus.OK);
    }

    @GetMapping("shipper/shipping/{id}")
    public ResponseEntity<SuccessResponse> getShippingById(HttpServletRequest request, @PathVariable UUID id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        ShippingEntity shipping = shippingService.findById(id);
        if(shipping.getUserOrderShipping()!= user)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Shipping not found",null),HttpStatus.NOT_FOUND);
        Map<String, Object> data = new HashMap<>();
        data.put("shipping",shippingService.shippingResponse(shipping));
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
        UserEntity user = userService.findById(request.getShipperID());
        if(user==null)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "User not Found",null),HttpStatus.NOT_FOUND);
        ShippingEntity shipping = shippingMapping.requestToEntity(order,user);
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

    @PutMapping("/shipper/update/{id}")
    public ResponseEntity<SuccessResponse> updateShipping(HttpServletRequest request,@PathVariable("id") int id,@RequestParam String img1,@RequestParam(required = false) String img2,@RequestParam(required = false) String img3){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        OrderEntity orderEntity = orderService.findById(id);
        ShippingEntity shipping = shippingService.findByShipper(user,orderEntity);
        if(orderEntity==null||orderEntity.getOrderStatus()!=1){
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Order Not Found",null),HttpStatus.NOT_FOUND);
        }
        else {
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
        UUID uuid = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        String url = imageStorageService.saveShippingImg(file, date.toString()+"/"+String.valueOf(uuid));
        if(url.equals(""))
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Upload Image Failure",null), HttpStatus.NOT_FOUND);
        Map<String, Object> data = new HashMap<>();
        data.put("url",url);
        return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Upload Logo Successfully",data), HttpStatus.OK);
    }

    @PostMapping(value = "/shipper/uploadImg/{id}")
    public ResponseEntity<SuccessResponse> uploadImgShippingV2(HttpServletRequest request,@PathVariable("id") int id, @RequestPart(required = true) MultipartFile file){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "User not found",null),HttpStatus.NOT_FOUND);
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
