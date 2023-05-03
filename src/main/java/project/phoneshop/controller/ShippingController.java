package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.mapping.ShipMapping;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.model.entity.ShipEntity;
import project.phoneshop.model.entity.ShippingEntity;
import project.phoneshop.model.payload.request.ship.AddShipRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.service.OrderService;
import project.phoneshop.service.ShipService;
import project.phoneshop.service.ShippingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ShippingController {
    private final ShippingService shippingService;
    private final OrderService orderService;
    @GetMapping("admin/shipping/list")
    public ResponseEntity<SuccessResponse> getAllShipping(){
        List<ShippingEntity> list = shippingService.getAllShipping();
        Map<String, Object> data = new HashMap<>();
        data.put("listShipping",list);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List shipping",data),HttpStatus.OK);
    }

    @GetMapping("shipping/{id}")
    public ResponseEntity<SuccessResponse> getShippingById(@PathVariable("id")int id, @RequestParam String secretKey){
        OrderEntity orderEntity = orderService.findShipping(id,secretKey);
        if(orderEntity==null){
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Order Not Found",null),HttpStatus.NOT_FOUND);
        }
        else {
            ShippingEntity shipping = shippingService.getInfoShippingByOrderId(orderEntity.getOrderId());
            Map<String,Object> data = new HashMap<>();
            data.put("shipping",shipping);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "shipping",data),HttpStatus.OK);
        }
    }

//    @PostMapping("/create")
//    public ResponseEntity<SuccessResponse> createShipType(@RequestBody AddShipRequest request){
//
//
//        ShipEntity ship = shipMapping.modelToEntity(request);
//        SuccessResponse response = new SuccessResponse();
//        try {
//            shipService.create(ship);
//            response.setStatus(HttpStatus.OK.value());
//            response.setMessage("add ship type successful");
//            response.setSuccess(true);
//            response.getData().put("Ship Type",ship.toString());
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<SuccessResponse> updateShipType(@RequestBody AddShipRequest request,@PathVariable("id") int id){
//
//
//        ShipEntity ship = shipMapping.updateToEntity(request,id);
//        SuccessResponse response = new SuccessResponse();
//        try {
//            shipService.update(ship);
//            response.setStatus(HttpStatus.OK.value());
//            response.setMessage("update ship type successful");
//            response.setSuccess(true);
//            response.getData().put("Ship Type",ship.toString());
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<SuccessResponse> deleteShipTypeById(@PathVariable("id")int id) throws Exception{
//        SuccessResponse response = new SuccessResponse();
//        try {
//            shipService.delete(id);
//            response.setMessage("Delete ship type success");
//            response.setStatus(HttpStatus.OK.value());
//            response.setSuccess(true);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }catch (Exception e){
//            throw new Exception(e.getMessage() + "\nDelete Ship type fail");
//        }
//    }
}
