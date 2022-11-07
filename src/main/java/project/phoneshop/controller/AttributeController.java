package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.mapping.AttributeMapping;
import project.phoneshop.model.entity.AttributeEntity;
import project.phoneshop.model.entity.AttributeOptionEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.request.attribute.AttributeOptionRequest;
import project.phoneshop.model.payload.request.attribute.AttributeRequest;
import project.phoneshop.model.payload.request.attribute.UpdateAttributeOptionRequest;
import project.phoneshop.model.payload.request.attribute.UpdateAttributeRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.service.AttributeService;
import project.phoneshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService attributeService;
    private final UserService userService;
    @Autowired
    AuthorizationHeader authorizationHeader;
    @GetMapping("/attribute/all")
    public ResponseEntity<SuccessResponse> getAllAttribute(){
        List<AttributeEntity> listAttribute = attributeService.findAllAttribute();
        if(listAttribute.size() == 0)
            return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.FOUND.value(), "List Attribute is Empty",null), HttpStatus.FOUND);
        Map<String, Object> data = new HashMap<>();
        data.put("listAttribute", listAttribute);
        return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Query Attribute Successfully", data ), HttpStatus.OK);
    }
    @GetMapping("/attribute/{id}")
    public ResponseEntity<SuccessResponse> getAllAttributeById(@PathVariable String id){
        AttributeEntity attribute = attributeService.findById(id);
        if(attribute == null)
            return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_FOUND.value(), "Attribute was not found",null), HttpStatus.NOT_FOUND);
        Map<String, Object> data = new HashMap<>();
        data.put("attribute", attribute);
        return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Query Attribute Successfully", data ), HttpStatus.OK);
    }
    @GetMapping("/attribute/option/all")
    public ResponseEntity<SuccessResponse> getAllAttributeOptions(){
        List<AttributeOptionEntity> listAttribute = attributeService.findAllAttributeOption();
        if(listAttribute.size() == 0)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"List Attribute is Empty",null), HttpStatus.NOT_FOUND);
        Map<String, Object> data = new HashMap<>();
        data.put("listAttribute", listAttribute);
        return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Query Attribute Successfully",data), HttpStatus.OK);
    }
    @PostMapping("/admin/attribute/insert")
    public ResponseEntity<SuccessResponse> insertAttributeFromJson(HttpServletRequest request, @RequestBody List<AttributeRequest> attributeRequests){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if (attributeRequests.isEmpty())
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_FOUND.value(),"List Attribute Is Empty",null ), HttpStatus.NOT_FOUND);
            for(AttributeRequest attributeRequest : attributeRequests) {
                AttributeEntity attribute = AttributeMapping.addNewAttribute(attributeRequest);
                attributeService.saveAttribute(attribute);
            }
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Insert Attribute Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/admin/attribute/option/insert")
    public ResponseEntity<SuccessResponse> insertAttributeOptions(HttpServletRequest request,@RequestBody List<AttributeOptionRequest> attributeOptionRequests){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if(attributeOptionRequests.isEmpty())
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_FOUND.value(),"List Attribute Options is Empty",null ), HttpStatus.NOT_FOUND);
            for(AttributeOptionRequest attributeOptionRequest : attributeOptionRequests){
                AttributeEntity attribute = attributeService.findById(attributeOptionRequest.getIdType());
                AttributeOptionEntity attributeOption = AttributeMapping.addNewAttributeOption(attributeOptionRequest,attribute);
                attributeService.saveAttributeOption(attributeOption);
            }
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Insert Attribute Options Successfully", null ), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PutMapping("/admin/attribute/update/{id}")
    public ResponseEntity<SuccessResponse> UpdateAttribute(HttpServletRequest request,@PathVariable String id, @RequestBody UpdateAttributeRequest attributeRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            AttributeEntity attribute = attributeService.findById(id);
            if(attribute == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Attribute not found",null ), HttpStatus.NOT_FOUND);
            attribute.setName(attributeRequest.getName());
            attributeService.saveAttribute(attribute);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Update Attribute Successfully",null ), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PutMapping("/admin/attribute/option/update/{id}")
    public ResponseEntity<SuccessResponse> UpdateAttributeOptions(HttpServletRequest request,@PathVariable String id, @RequestBody UpdateAttributeOptionRequest attributeRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            AttributeOptionEntity attributeOption = attributeService.findByIdAttributeOption(id);
            if(attributeOption == null)
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_FOUND.value(),"Attribute Option not found",null ), HttpStatus.NOT_FOUND);
            attributeOption.setValue(attributeRequest.getValue());
            attributeService.saveAttributeOption(attributeOption);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Update Attribute Option Successfully",null ), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @DeleteMapping("/admin/attribute/delete/{id}")
    public ResponseEntity<SuccessResponse> deleteAttributeById(HttpServletRequest request,@PathVariable String id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            AttributeEntity attribute = attributeService.findById(id);
            SuccessResponse response=new SuccessResponse();
            if(attribute == null)
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_FOUND.value(), "Attribute Not found",null), HttpStatus.NOT_FOUND);
            try {
                attributeService.deleteAttribute(id);
            }
            catch(Exception e){
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_ACCEPTABLE.value(), "Attribute is not deleted", null), HttpStatus.NOT_ACCEPTABLE);
            }
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Attribute is deleted", null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @DeleteMapping("/admin/attribute/option/delete/{id}")
    public ResponseEntity<SuccessResponse> deleteAttributeOptionById(HttpServletRequest request,@PathVariable String id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            AttributeOptionEntity attribute = attributeService.findByIdAttributeOption(id);
            if(attribute == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Attribute option Not found",null ), HttpStatus.NOT_FOUND);
            try {
                attributeService.deleteAttribute(id);
            }
            catch(Exception e){
                return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.NOT_ACCEPTABLE.value(), "Attribute option is not deleted", null), HttpStatus.NOT_ACCEPTABLE);
            }
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Attribute option is deleted",null ), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
