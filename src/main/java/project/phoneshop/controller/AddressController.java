package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.handler.HttpMessageNotReadableException;
import project.phoneshop.mapping.AddressMapping;
import project.phoneshop.model.entity.AddressEntity;
import project.phoneshop.model.entity.AddressTypeEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.request.address.AddNewAddressRequest;
import project.phoneshop.model.payload.request.address.InfoAddressRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.service.AddressService;
import project.phoneshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final UserService userService;
    @Autowired
    AuthorizationHeader authorizationHeader;
    @PostMapping("/insert")
    @ResponseBody
    public ResponseEntity<SuccessResponse> saveAddress(@RequestBody @Valid AddNewAddressRequest addNewAddressRequest, BindingResult errors, HttpServletRequest request) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null) throw new BadCredentialsException("User not found");
        else{
            if (addNewAddressRequest == null) throw new HttpMessageNotReadableException("Missing field");
            try {
                AddressTypeEntity addressType = addressService.findByTid(addNewAddressRequest.getAddressType());
                SuccessResponse response = new SuccessResponse();
                AddressEntity address = AddressMapping.ModelToEntity(addNewAddressRequest,addressType);
                address.setUser(user);
                if (address==null) return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_FOUND.value(),"Add address failure",null), HttpStatus.NOT_FOUND);
                user.getAddress().add(address);
                try {userService.saveInfo(user);}
                catch (Exception e) {return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.CONFLICT.value(),"Address limit reach",null), HttpStatus.CONFLICT);}
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Add address success",null), HttpStatus.OK);
            } catch (Exception e) {throw new Exception(e.getMessage() + "\nAdd address fail");}
        }
    }
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<SuccessResponse> getUserAddress(HttpServletRequest request) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
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
    @PutMapping("update/{id}")
    @ResponseBody
    public ResponseEntity<SuccessResponse> updateUserAddress(@RequestBody @Valid InfoAddressRequest infoAddressRequest, @PathVariable("id") UUID id, HttpServletRequest request) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            AddressEntity address = addressService.findById(id);
            AddressTypeEntity addressType = addressService.findByTid(infoAddressRequest.getAddressType());
            address = AddressMapping.UpdateAddressEntity(address,infoAddressRequest,addressType);
            if (address==null)
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_FOUND.value(),"Update address failure",null), HttpStatus.NOT_FOUND);
            addressService.saveAddress(address);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Update address successfully",null), HttpStatus.OK);
        }
    }
    @DeleteMapping("delete/{id}")
    @ResponseBody
    public ResponseEntity<SuccessResponse> deleteAddress(@PathVariable("id") UUID id, HttpServletRequest request) throws Exception {
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            try {
                addressService.deleteAddress(id);
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Delete address success",null), HttpStatus.OK);
            } catch (Exception e) {
                throw new Exception(e.getMessage() + "\nDelete address failure");
            }
        }
    }
}
