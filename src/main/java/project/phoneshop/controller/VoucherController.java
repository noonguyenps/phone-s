package project.phoneshop.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.mapping.VoucherMapping;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.entity.UserNotificationEntity;
import project.phoneshop.model.entity.VoucherEntity;
import project.phoneshop.model.payload.request.voucher.UpdateVoucherRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.service.UserNotificationService;
import project.phoneshop.service.UserService;
import project.phoneshop.service.VoucherService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;
    private final UserService userService;
    private final UserNotificationService userNotificationService;
    @Autowired
    AuthorizationHeader authorizationHeader;
    @GetMapping("/voucher/all")
    public ResponseEntity<SuccessResponse> showAllVoucherPublic() {
        List<VoucherEntity> listVoucher = voucherService.findAllVoucherPublic();
        if(listVoucher.size() == 0)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"Voucher is empty",null), HttpStatus.FOUND);
        Map<String,Object> data = new HashMap<>();
        data.put("listVoucher",listVoucher);
        return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
    }
    @GetMapping("/voucher/{id}")
    public ResponseEntity<SuccessResponse> getVoucherById(@PathVariable UUID id){
        VoucherEntity voucher = voucherService.findById(id);
        if(voucher == null || !voucher.isStatus())
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Voucher not found",null), HttpStatus.NOT_FOUND);
        Map<String, Object>  data = new HashMap<>();
        data.put("voucher", voucher);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Get voucher Successfully",null), HttpStatus.OK);
    }
    @PostMapping("/user/get/voucher/{id}")
    public ResponseEntity<SuccessResponse> getVoucher(HttpServletRequest request,@PathVariable UUID id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            VoucherEntity voucher = voucherService.findById(id);
            if(voucher == null || !voucher.isStatus())
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Voucher not found",null), HttpStatus.NOT_FOUND);
            Set<UserEntity> newList = voucher.getUserEntities();
            newList.add(user);
            voucher.setAmount(voucher.getAmount()-1);
            voucher.setUserEntities(newList);
            voucherService.saveVoucher(voucher);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Get voucher Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/admin/voucher/all")
    public ResponseEntity<SuccessResponse> showAllVoucher(HttpServletRequest request) {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<VoucherEntity> listVoucher = voucherService.findAllVoucher();
            if(listVoucher.size() == 0)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"Voucher is empty",null), HttpStatus.FOUND);
            Map<String, Object> data = new HashMap<>();
            data.put("listVoucher",listVoucher);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List Voucher",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/admin/add/voucher/insert")
    private ResponseEntity<SuccessResponse> insertVoucher(HttpServletRequest request,@RequestBody VoucherEntity newVoucher){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<VoucherEntity> foundVoucher = voucherService.foundVoucher(newVoucher.getType());
            if(foundVoucher.size() > 0)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"Voucher found",null), HttpStatus.FOUND);
            voucherService.saveVoucher(newVoucher);
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.OK.value(),"Add voucher successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PutMapping("/admin/voucher/update/{id}")
    private ResponseEntity<SuccessResponse> updateVoucher(HttpServletRequest request, @PathVariable UUID id, @RequestBody UpdateVoucherRequest updateVoucherRequest) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            VoucherEntity voucher = voucherService.findById(id);
            if(voucher != null){
                voucher = VoucherMapping.updateVoucher(voucher,updateVoucherRequest);
                voucherService.saveVoucher(voucher);
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Update voucher successfully",null), HttpStatus.OK);
            }else
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Voucher not found",null), HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @DeleteMapping("/admin/voucher/{id}")
    @ResponseBody
    public ResponseEntity<SuccessResponse> deleteVoucher(HttpServletRequest request,@PathVariable UUID id,@RequestParam String message) {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            VoucherEntity voucher = voucherService.findById(id);
            if(voucher != null) {
                for(UserEntity userNotification: voucher.getUserEntities()){
                    UserNotificationEntity userNotificationEntity = new UserNotificationEntity(message,"system",userNotification);
                    userNotificationService.create(userNotificationEntity, userNotification);
                }
                voucherService.deleteVoucher(id);
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Delete voucher successfully",null), HttpStatus.OK);
            }else
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_ACCEPTABLE.value(),"Delete voucher failure",null), HttpStatus.NOT_ACCEPTABLE);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/admin/send/voucher/{id}")
    public ResponseEntity<SuccessResponse> sendVoucher(HttpServletRequest request,@PathVariable UUID id,@RequestParam UUID idUser){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            VoucherEntity voucher = voucherService.findById(id);
            if(voucher == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.OK.value(),"Voucher Not Found",null), HttpStatus.OK);
            UserEntity userAcc = userService.findById(idUser);
            if(userAcc == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "User not found",null),HttpStatus.NOT_FOUND);
            Set<UserEntity> newList = voucher.getUserEntities();
            newList.add(userAcc);
            voucher.setAmount(voucher.getAmount()-1);
            voucher.setUserEntities(newList);
            voucherService.saveVoucher(voucher);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Send voucher successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/user/voucher/all")
    public ResponseEntity<SuccessResponse> getVoucherByUser(HttpServletRequest request){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            Map<String, Object> data = new HashMap<>();
            List<VoucherEntity> listVoucher = voucherService.findAllVoucherBtUser(user);
            if(listVoucher.isEmpty())
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "List voucher is Empty",null),HttpStatus.NOT_FOUND);
            data.put("listVoucher",listVoucher);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List voucher",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}




