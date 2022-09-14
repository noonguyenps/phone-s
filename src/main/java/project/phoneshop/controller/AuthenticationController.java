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
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.request.authentication.PhoneLoginRequest;
import project.phoneshop.model.payload.response.ErrorResponseMap;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.security.DTO.AppUserDetail;
import project.phoneshop.security.JWT.JwtUtils;
import project.phoneshop.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
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
//    private final EmailService emailService;
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

        UserEntity loginUser=userService.findByPhone(user.getPhone());
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
        response.getData().put("user",loginUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
//    @PostMapping("/verification")
//    public ResponseEntity<SuccessResponse> verifyPhoneNumber(@RequestBody @Valid VerifyPhoneRequest request) {
//        UserEntity user=userService.findByPhone(request.getPhone());
//        SuccessResponse response=new SuccessResponse();
//        if(user!=null){
//            response.setMessage("This phone already exists"+" ("+user.getPhone()+")");
//            response.setSuccess(true);
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
//        else{
//            response.setMessage("You can create an account with this phone number");
//            response.setSuccess(true);
//            response.setStatus(HttpStatus.OK.value());
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
//    }
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
//    @PostMapping("/refreshtoken")
//    public ResponseEntity<SuccessResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken,
//                                                        HttpServletRequest request, HttpServletResponse resp){
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
//            String accessToken = authorizationHeader.substring("Bearer ".length());
//
//            if(!jwtUtils.validateExpiredToken(accessToken)){
//                throw new BadCredentialsException("access token is not expired");
//            }
//
//            if(jwtUtils.validateExpiredToken(refreshToken.getRefreshToken())){
//                throw new BadCredentialsException("refresh token is expired");
//            }
//
//            if(refreshToken == null){
//                throw new BadCredentialsException("refresh token is missing");
//            }
//
//            if(!jwtUtils.getUserNameFromJwtToken(refreshToken
//                    .getRefreshToken()).equals(jwtUtils.getUserNameFromJwtToken(refreshToken.getRefreshToken()))){
//                throw new BadCredentialsException("two token are not a pair");
//            }
//
//
//            AppUserDetail userDetails =  AppUserDetail.build(userService
//                    .findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(refreshToken.getRefreshToken()))));
//
//            accessToken = jwtUtils.generateJwtToken(userDetails);
//
//            SuccessResponse response = new SuccessResponse();
//            response.setStatus(HttpStatus.OK.value());
//            response.setMessage("Login successful");
//            response.setSuccess(true);
//
//            Cookie cookieAccessToken = new Cookie("accessToken", accessToken);
//
//            resp.setHeader("Set-Cookie", "test=value; Path=/");
//            resp.addCookie(cookieAccessToken);
//
//            response.getData().put("accessToken",accessToken);
//            response.getData().put("refreshToken",refreshToken.getRefreshToken());
//
//
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
//        else
//        {
//            throw new BadCredentialsException("access token is missing");
//        }
//    }
//    @PostMapping("/refreshtokencookie")
//    public ResponseEntity<SuccessResponse> refreshTokenCookie(@CookieValue("refreshToken") String refreshToken, HttpServletRequest request) {
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
//            String accessToken = authorizationHeader.substring("Bearer ".length());
//
//            if(!jwtUtils.validateExpiredToken(accessToken)){
//                throw new BadCredentialsException("access token is not expired");
//            }
//
//            if(jwtUtils.validateExpiredToken(refreshToken)){
//                throw new BadCredentialsException("refresh token is expired");
//            }
//
//            if(refreshToken == null){
//                throw new BadCredentialsException("refresh token is missing");
//            }
//
//            if(!jwtUtils.getUserNameFromJwtToken(refreshToken).equals(jwtUtils.getUserNameFromJwtToken(refreshToken))){
//                throw new BadCredentialsException("two token are not a pair");
//            }
//
//
//            AppUserDetail userDetails =  AppUserDetail.build(userService
//                    .findById(UUID.fromString(jwtUtils.getUserNameFromJwtToken(refreshToken))));
//
//            accessToken = jwtUtils.generateJwtToken(userDetails);
//
//            SuccessResponse response = new SuccessResponse();
//            response.setStatus(HttpStatus.OK.value());
//            response.setMessage("Login successful");
//            response.setSuccess(true);
//
//            response.getData().put("accessToken",accessToken);
//            response.getData().put("refreshToken",refreshToken);
//
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
//        else
//        {
//            throw new BadCredentialsException("access token is missing");
//        }
//    }
//    @GetMapping("/active")
//    public ResponseEntity<SuccessResponse> activeToken( @RequestParam(defaultValue = "") String key
//    ) {
//        if(key == null || key ==""){
//            throw new BadCredentialsException("key active is not valid");
//        }
//
//        UUID id = UUID.fromString(jwtUtils.getUserNameFromJwtToken(key));
//        UserEntity user = userService.findById(id);
//
//        if(user == null){
//            throw new RecordNotFoundException("Not found, please register again");
//        }
//
//        if(user.isActive()){
//            throw new RecordNotFoundException("user already has been activated!");
//        }
//
//        userService.updateActive(user);
//
//
//
//        SuccessResponse response = new SuccessResponse();
//        response.setStatus(HttpStatus.OK.value());
//        response.setMessage("Active successful");
//        response.setSuccess(true);
//
//        response.getData().put("email",user.getEmail());
//
//        return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
//    }
//    @GetMapping("/social")
//    public ResponseEntity<SuccessResponse> socialToken(
//                                                       @RequestParam(defaultValue = "") String token,
//                                                       HttpServletResponse resp) {
//        if(token == null || token.equals("")){
//            throw new BadCredentialsException("token is not valid");
//        }
//        String email= jwtUtils.getUserNameFromJwtToken(token);
//        UserEntity user = userService.findByEmail(email);
//
//        if(user == null){
//            throw new RecordNotFoundException("Not found, please register again");
//        }
//        AppUserDetail userDetails =  AppUserDetail.build(user);
//
//        String accessToken = jwtUtils.generateJwtToken(userDetails);
//        String refreshToken=jwtUtils.generateRefreshJwtToken(userDetails);
//
//        System.out.println(jwtUtils.getUserNameFromJwtToken(accessToken));
//        SuccessResponse response = new SuccessResponse();
//        response.setStatus(HttpStatus.OK.value());
//        response.setMessage("Login successful");
//        response.setSuccess(true);
//
//        Cookie cookieAccessToken = new Cookie("accessToken", accessToken);
//        Cookie cookieRefreshToken = new Cookie("refreshToken", refreshToken);
//
//        resp.setHeader("Set-Cookie", "test=value; Path=/");
//        resp.addCookie(cookieAccessToken);
//        resp.addCookie(cookieRefreshToken);
//
//        response.getData().put("accessToken",accessToken);
//        response.getData().put("refreshToken",refreshToken);
//        response.getData().put("user",user);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
////
//    @PostMapping("/forgetPassword")
//    public ResponseEntity<SuccessResponse> forgetPassword(@RequestBody @Valid ReActiveRequest request, BindingResult errors) throws Exception{
//        if (errors.hasErrors()) {
//            throw new MethodArgumentNotValidException(errors);
//        }
//        if (request == null) {
//            throw new HttpMessageNotReadableException("Missing field");
//        }
//
//        if(userService.findByEmail(request.getEmail())==null){
//            throw new HttpMessageNotReadableException("Email is not Registered");
//        }
//        UserEntity user=userService.findByEmail(request.getEmail());
//        try{
//            emailService.sendForgetPasswordMessage(user);
//
//            SuccessResponse response = new SuccessResponse();
//            response.setStatus(HttpStatus.OK.value());
//            response.setMessage("Send email with new password successful");
//            response.setSuccess(true);
//            response.getData().put("email",user.getEmail());
//            return new ResponseEntity<>(response, HttpStatus.OK);
//
//
//        }
//        catch (Exception ex){
//            SuccessResponse response = new SuccessResponse();
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
//            response.setMessage("Failed to send reset password email");
//            response.setSuccess(false);
//            throw  new Exception(ex.toString());
//        }
//
//    }
//    @PostMapping("/resetPassword")
//    public ResponseEntity<SuccessResponse> resetPassword(@RequestParam(defaultValue = "") String token, @RequestBody @Valid ResetPasswordRequest req,
//             BindingResult errors) throws Exception{
//        SuccessResponse response = new SuccessResponse();
//
//        if (errors.hasErrors()) {
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
//            response.setSuccess(true);
//            response.setMessage("Invalid");
//        }
//        if(token == null || token.equals("")){
//            throw new BadCredentialsException("token is not valid");
//        }
//        String email= jwtUtils.getUserNameFromJwtToken(token);
//        UserEntity user = userService.findByEmail(email);
//        if(user == null){
//            throw new RecordNotFoundException("User not found, please check again");
//        }
//        if(req.getNewPassword().equals(req.getConfirmPassword())){
//            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
//            userService.saveInfo(user);
//            response.setStatus(HttpStatus.OK.value());
//            response.setMessage("Reset password successful");
//            response.setSuccess(true);
//            response.getData().put("email",user.getEmail());
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
//        else{
//            throw new BadCredentialsException("New password doesn't match confirm password");
//        }
//    }
}

