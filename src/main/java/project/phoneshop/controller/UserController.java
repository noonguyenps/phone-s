package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.handler.HttpMessageNotReadableException;
import project.phoneshop.handler.MethodArgumentNotValidException;
import project.phoneshop.handler.RecordNotFoundException;
import project.phoneshop.mapping.UserMapping;
import project.phoneshop.model.entity.ProductEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.request.authentication.ReActiveRequest;
import project.phoneshop.model.payload.request.user.*;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.model.payload.response.product.ProductResponse;
import project.phoneshop.security.JWT.JwtUtils;
import project.phoneshop.service.EmailService;
import project.phoneshop.service.ImageStorageService;
import project.phoneshop.service.ProductService;
import project.phoneshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthorizationHeader authorizationHeader;
    private final ImageStorageService imageStorageService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ProductService productService;
    private final EmailService emailService;
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> registerAccount(@RequestBody @Valid AddNewUserRequest request) {
        UserEntity user= UserMapping.registerToEntity(request);
        if(userService.existsByPhone(user.getPhone()))
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.CONFLICT.value(),"Register Failure! Phone number is exist",null), HttpStatus.CONFLICT);;
        try{
            userService.saveUser(user,"USER");
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Register Successfully",null), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_ACCEPTABLE.value(),"Register Failure",null), HttpStatus.NOT_ACCEPTABLE);
    }
    @GetMapping("/profile")
    public ResponseEntity<SuccessResponse> getUserProfile(HttpServletRequest request) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            Map<String,Object> data = new HashMap<>();
            data.put("user",user);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Profile User",data), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("/profile/changePassword")
    public ResponseEntity<SuccessResponse> changePassword(HttpServletRequest request, @RequestBody @Valid ChangePasswordRequest changePasswordRequest) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            if(!passwordEncoder.matches(changePasswordRequest.getOldPassword(),user.getPassword()))
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_ACCEPTABLE.value(),"Old password is incorrect",null), HttpStatus.NOT_ACCEPTABLE);
            if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword()))
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_ACCEPTABLE.value(),"Confirm password is incorrect",null), HttpStatus.NOT_ACCEPTABLE);
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userService.saveInfo(user);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Change password successfully",null), HttpStatus.OK);
        }
    }
    @PostMapping("/profile/uploadAvatar")
    public ResponseEntity<SuccessResponse> uploadAvatar(HttpServletRequest request, @RequestPart MultipartFile file) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            if(!imageStorageService.isImageFile(file))
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),"The file is not an image",null), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            String url = imageStorageService.saveAvatar(file,user.getPhone());
            if(url.equals(""))
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.NOT_FOUND.value(),"Upload Avatar Failure",null), HttpStatus.NOT_FOUND);
            user.setImg(url);
            userService.saveInfo(user);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Upload Avatar successfully",null), HttpStatus.OK);
        }
    }
    @PutMapping("/profile/changeInfo")
    public ResponseEntity<SuccessResponse> changeInfo(HttpServletRequest request, @RequestBody @Valid ChangeInfoRequest changeInfoRequest) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            user = UserMapping.updateInfoUser(user,changeInfoRequest);
            userService.saveInfo(user);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Change Info successfully",null), HttpStatus.OK);
        }
    }
    @PutMapping("/profile/changeEmail")
    public ResponseEntity<SuccessResponse> changeEmail(HttpServletRequest request, @RequestBody @Valid ChangeEmailRequest changeEmailRequest) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            if(userService.findByEmail(changeEmailRequest.getEmail())!=null)
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.FOUND.value(),"Email is already used",null), HttpStatus.FOUND);
            user.setEmail(changeEmailRequest.getEmail());
            userService.saveInfo(user);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Change Email successfully",null), HttpStatus.OK);
        }
    }
    @PutMapping("/profile/changePhone")
    public ResponseEntity<SuccessResponse> changePhone(HttpServletRequest request, @RequestBody ChangePhoneRequest changePhoneRequest) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            if(userService.findByPhone(changePhoneRequest.getPhone())!=null ){
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.FOUND.value(),"Phone is already used",null), HttpStatus.FOUND);
            }
            user.setPhone(changePhoneRequest.getPhone());
            userService.saveInfo(user);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Change Phone successfully",null), HttpStatus.OK);
        }
    }
    @GetMapping("/wishlist")
    public ResponseEntity<SuccessResponse> getWishlist(HttpServletRequest request) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            List<ProductResponse> productResponseList = new ArrayList<>();
            for(ProductEntity product : user.getFavoriteProducts()){
                productResponseList.add(productService.productResponse(product));
            }
            Map<String,Object> data = new HashMap<>();
            data.put("listProduct",productResponseList);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "Get wishlist successfully",data),HttpStatus.OK);
        }
    }
    @PostMapping("/wishlist/add")
    public ResponseEntity<SuccessResponse> addToWishList(HttpServletRequest request,@RequestParam UUID productId) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            ProductEntity product = productService.findById(productId);
            if(product == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Product Not Found",null),HttpStatus.NOT_FOUND);
            user.getFavoriteProducts().add(product);
            userService.saveInfo(user);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "Add wishlist successfully",null),HttpStatus.OK);
        }
    }
    @DeleteMapping("/wishlist/remove")
    public ResponseEntity<SuccessResponse> removeToWishList(HttpServletRequest request,@RequestParam UUID productId) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            ProductEntity product = productService.findById(productId);
            if(product == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Product Not Found",null),HttpStatus.NOT_FOUND);
            user.getFavoriteProducts().remove(product);
            userService.saveInfo(user);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "Remove wishlist successfully",null),HttpStatus.OK);
        }
    }
    @GetMapping("/wishlist/check")
    public ResponseEntity<SuccessResponse> checkWishList(HttpServletRequest request,@RequestParam UUID productId) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            ProductEntity product = productService.findById(productId);
            if(product == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Product Not Found",null),HttpStatus.NOT_FOUND);
            if(user.getFavoriteProducts().contains(product)){
                List<UUID> listProduct = new ArrayList<>();
                listProduct.add(product.getId());
                Map<String,Object> data = new HashMap<>();
                data.put("product",listProduct);
                return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "Query wishlist successfully",data),HttpStatus.OK);
            }
            Map<String,Object> data = new HashMap<>();
            data.put("product",new ArrayList<>());
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "Query wishlist successfully",data), HttpStatus.OK);
        }
    }
    @GetMapping("/phone/check")
    public ResponseEntity<SuccessResponse> checkPhone(HttpServletRequest request) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            if(user.getPassword()==null) {
                return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "User no password", null), HttpStatus.OK);
            }
            return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.FOUND.value(), "User exist password", null), HttpStatus.FOUND);
        }
    }
    @PutMapping("/profile/changePhonePassword")
    public ResponseEntity<SuccessResponse> updatePhonePassword(HttpServletRequest request,@RequestBody UpdatePhonePassword updatePhonePassword){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        else{
            if (user.getPassword()==null){
                if(userService.existsByPhone(updatePhonePassword.getPhone())){
                    return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(), "Phone number is exist",null),HttpStatus.FOUND);
                }
                if(updatePhonePassword.getPassword().equals(updatePhonePassword.getRetypePassword())){
                    user.setPassword(passwordEncoder.encode(updatePhonePassword.getPassword()));
                    user.setPhone(updatePhonePassword.getPhone());
                    userService.saveInfo(user);
                    return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Update Successfully", null), HttpStatus.OK);
                }
            }

        }
        return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.FOUND.value(), "Update Failure", null), HttpStatus.FOUND);
    }
    @PostMapping("/verification/email")
    public ResponseEntity<SuccessResponse> verificationEmail(HttpServletRequest request,@RequestBody @Valid ReActiveRequest reRequest, BindingResult errors) throws Exception{
        UserEntity user=authorizationHeader.AuthorizationHeader(request);
        if(user==null)
            throw new BadCredentialsException("User not found");
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        if(userService.findByEmail(reRequest.getData())!=null){
            throw new HttpMessageNotReadableException("Email is Existed");
        }

        try{
            emailService.sendmailVerification(user,reRequest.getData());
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "Send email successfully",null),HttpStatus.OK);
        }
        catch (Exception ex){
            throw  new Exception(ex.toString());
        }
    }
}
