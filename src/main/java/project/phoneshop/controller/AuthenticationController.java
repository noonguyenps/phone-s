package project.phoneshop.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.handler.HttpMessageNotReadableException;
import project.phoneshop.handler.MethodArgumentNotValidException;
import project.phoneshop.handler.RecordNotFoundException;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.entity.VoucherEntity;
import project.phoneshop.model.payload.request.authentication.PhoneLoginRequest;
import project.phoneshop.model.payload.request.authentication.ReActiveRequest;
import project.phoneshop.model.payload.request.authentication.RefreshTokenRequest;
import project.phoneshop.model.payload.request.authentication.VerifyPhoneRequest;
import project.phoneshop.model.payload.request.user.ResetPasswordRequest;
import project.phoneshop.model.payload.response.ErrorResponseMap;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.model.payload.response.user.UserResponse;
import project.phoneshop.security.DTO.AppUserDetail;
import project.phoneshop.security.JWT.JwtUtils;
import project.phoneshop.service.EmailService;
import project.phoneshop.service.Impl.VoucherServiceImpl;
import project.phoneshop.service.UserService;
import project.phoneshop.service.VoucherService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@ComponentScan
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EmailService emailService;
    private final VoucherService voucherService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody @Valid PhoneLoginRequest user, BindingResult errors, HttpServletResponse resp) {
        if(errors.hasErrors()) {
            return null;
        }
        if(!userService.existsByPhone(user.getPhone())) {
            return SendErrorValid("Phone", user.getPhone()+" not found","No account found" );
        }

        UserEntity loginUser= userService.findByPhone(user.getPhone());
        if(!passwordEncoder.matches(user.getPassword(),loginUser.getPassword())) {
            return SendErrorValid("password", user.getPassword()+" incorrect","Wrong password" );
        }
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getId().toString(),user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetail userDetail= (AppUserDetail) authentication.getPrincipal();

        String accessToken = jwtUtils.generateJwtToken(userDetail);
        String refreshToken=jwtUtils.generateRefreshJwtToken(userDetail);

        System.out.println(jwtUtils.getUserNameFromJwtToken(accessToken));
        SuccessResponse response = new SuccessResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Login successful");
        response.setSuccess(true);

        Cookie cookieAccessToken = new Cookie("accessToken", accessToken);
        Cookie cookieRefreshToken = new Cookie("refreshToken", refreshToken);

        resp.setHeader("Set-Cookie", "test=value; Path=/");
        resp.addCookie(cookieAccessToken);
        resp.addCookie(cookieRefreshToken);

        response.getData().put("accessToken",accessToken);
        response.getData().put("refreshToken",refreshToken);
        List<VoucherEntity> listVoucher = voucherService.findAllVoucherBtUser(loginUser);
        UserResponse userResponse = userService.getUserResponse(loginUser);
        userResponse.setCountVoucher(listVoucher.size());
        response.getData().put("user",userResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/verification")
    public ResponseEntity<SuccessResponse> verifyPhoneNumber(@RequestBody @Valid VerifyPhoneRequest request) {
        UserEntity user=userService.findByPhone(request.getPhone());
        if(user!=null)
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.FOUND.value(),"This phone already exists",null),HttpStatus.OK);
        else
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"This phone ok",null),HttpStatus.OK);
    }
    private ResponseEntity SendErrorValid(String field, String message,String title){
        ErrorResponseMap errorResponseMap = new ErrorResponseMap();
        Map<String,String> temp =new HashMap<>();
        errorResponseMap.setMessage(title);
        temp.put(field,message);
        errorResponseMap.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseMap.setDetails(temp);
        return ResponseEntity
                .badRequest()
                .body(errorResponseMap);
    }
    @PostMapping("/refreshtoken")
    public ResponseEntity<SuccessResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken,
                                                        HttpServletRequest request, HttpServletResponse resp){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(!jwtUtils.validateExpiredToken(accessToken)){
                throw new BadCredentialsException("access token is not expired");
            }

            if(jwtUtils.validateExpiredToken(refreshToken.getRefreshToken())){
                throw new BadCredentialsException("refresh token is expired");
            }

            if(refreshToken == null){
                throw new BadCredentialsException("refresh token is missing");
            }

            if(!jwtUtils.getUserNameFromJwtToken(refreshToken
                    .getRefreshToken()).equals(jwtUtils.getUserNameFromJwtToken(refreshToken.getRefreshToken()))){
                throw new BadCredentialsException("two token are not a pair");
            }


            AppUserDetail userDetails =  AppUserDetail.build(userService
                    .findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(refreshToken.getRefreshToken()))));

            accessToken = jwtUtils.generateJwtToken(userDetails);

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Login successful");
            response.setSuccess(true);

            Cookie cookieAccessToken = new Cookie("accessToken", accessToken);

            resp.setHeader("Set-Cookie", "test=value; Path=/");
            resp.addCookie(cookieAccessToken);

            response.getData().put("accessToken",accessToken);
            response.getData().put("refreshToken",refreshToken.getRefreshToken());


            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }
    @PostMapping("/refreshtokencookie")
    public ResponseEntity<SuccessResponse> refreshTokenCookie(@CookieValue("refreshToken") String refreshToken, HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(!jwtUtils.validateExpiredToken(accessToken)){
                throw new BadCredentialsException("access token is not expired");
            }

            if(jwtUtils.validateExpiredToken(refreshToken)){
                throw new BadCredentialsException("refresh token is expired");
            }

            if(refreshToken == null){
                throw new BadCredentialsException("refresh token is missing");
            }

            if(!jwtUtils.getUserNameFromJwtToken(refreshToken).equals(jwtUtils.getUserNameFromJwtToken(refreshToken))){
                throw new BadCredentialsException("two token are not a pair");
            }


            AppUserDetail userDetails =  AppUserDetail.build(userService
                    .findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(refreshToken))));

            accessToken = jwtUtils.generateJwtToken(userDetails);

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Login successful");
            response.setSuccess(true);

            response.getData().put("accessToken",accessToken);
            response.getData().put("refreshToken",refreshToken);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }
    @GetMapping("/active")
    public ResponseEntity<SuccessResponse> activeToken( @RequestParam(defaultValue = "") String key
    ) {
        if(key == null || key ==""){
            throw new BadCredentialsException("key active is not valid");
        }

        UUID id = UUID.fromString(jwtUtils.getUserNameFromJwtToken(key));
        UserEntity user = userService.findById(id);

        if(user == null){
            throw new RecordNotFoundException("Not found, please register again");
        }

        if(user.isActive()){
            throw new RecordNotFoundException("user already has been activated!");
        }

        userService.updateActive(user);



        SuccessResponse response = new SuccessResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Active successful");
        response.setSuccess(true);

        response.getData().put("email",user.getEmail());

        return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
    }
    @GetMapping("/social")
    public ResponseEntity<SuccessResponse> socialToken(@RequestParam(defaultValue = "") String token,
                                                       HttpServletResponse resp) {
        if(token == null || token.equals("")){
            throw new BadCredentialsException("token is not valid");
        }
        String email= jwtUtils.getUserNameFromJwtToken(token);
        UserEntity user = userService.findByEmail(email);

        if(user == null){
            throw new RecordNotFoundException("Not found, please register again");
        }
        AppUserDetail userDetails =  AppUserDetail.build(user);

        String accessToken = jwtUtils.generateJwtToken(userDetails);
        String refreshToken=jwtUtils.generateRefreshJwtToken(userDetails);

        System.out.println(jwtUtils.getUserNameFromJwtToken(accessToken));
        SuccessResponse response = new SuccessResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Login successful");
        response.setSuccess(true);

        Cookie cookieAccessToken = new Cookie("accessToken", accessToken);
        Cookie cookieRefreshToken = new Cookie("refreshToken", refreshToken);

        resp.setHeader("Set-Cookie", "test=value; Path=/");
        resp.addCookie(cookieAccessToken);
        resp.addCookie(cookieRefreshToken);

        response.getData().put("accessToken",accessToken);
        response.getData().put("refreshToken",refreshToken);
        response.getData().put("user",user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/forgetPassword")
    public ResponseEntity<SuccessResponse> forgetPassword(@RequestBody @Valid ReActiveRequest request, BindingResult errors) throws Exception{
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        if (request == null) {
            throw new HttpMessageNotReadableException("Missing field");
        }
        if(userService.findByEmail(request.getEmail())==null){
            throw new HttpMessageNotReadableException("Email is not Registered");
        }
        UserEntity user=userService.findByEmail(request.getEmail());
        try{
            emailService.sendmail(user);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "Send email with new password successfully",null),HttpStatus.OK);
        }
        catch (Exception ex){
            throw  new Exception(ex.toString());
        }

    }
    @PostMapping("/resetPassword")
    public ResponseEntity<SuccessResponse> resetPassword(@RequestParam(defaultValue = "") String token, @RequestBody @Valid ResetPasswordRequest req,
             BindingResult errors) throws Exception{
        if (errors.hasErrors()) {
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.BAD_REQUEST.value(), "Invalid",null),HttpStatus.BAD_REQUEST);
        }
        if(token == null || token.equals("")){
            throw new BadCredentialsException("token is not valid");
        }
        String email= jwtUtils.getUserNameFromJwtToken(token);
        UserEntity user = userService.findByEmail(email);
        if(user == null){
            throw new RecordNotFoundException("User not found, please check again");
        }
        if(req.getNewPassword().equals(req.getConfirmPassword())){
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
            userService.saveInfo(user);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Reset password successfully",null),HttpStatus.OK);
        }
        else{
            throw new BadCredentialsException("New password doesn't match confirm password");
        }
    }
}

