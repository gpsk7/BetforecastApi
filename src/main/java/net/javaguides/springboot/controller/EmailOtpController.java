package net.javaguides.springboot.controller;

import net.javaguides.springboot.exception.DuplicateMobileNumberException;
import net.javaguides.springboot.request.OtpVerificationRequest;
import net.javaguides.springboot.request.RegisterRequest;
import net.javaguides.springboot.response.RegistrationResponse;
import net.javaguides.springboot.service.EmailOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
    this controller is for user otp verification, first user is stored in  EmailOtp table until
    user is verified, if user verified then user is pushed to actual user table (User)
 **/
@RestController
@RequestMapping("/api/v1/auth")
public class EmailOtpController {
    @Autowired
    private EmailOtpService emailOtpService;
//    @PostMapping("/register")
//    public ResponseEntity<RegistrationResponse> register(@RequestBody RegisterRequest request){
//        return ResponseEntity.ok(emailOtpService.register(request));
//    }
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegisterRequest request) {
        try {
            RegistrationResponse response = emailOtpService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegistrationResponse(ex.getMessage()));
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<RegistrationResponse> verify(@RequestBody OtpVerificationRequest request){
          if(emailOtpService.verifyOTP(request.getUserId(),request.getOtp())){
              return ResponseEntity.ok(new RegistrationResponse("User Registered successfully"));
          }
        return new ResponseEntity<>(new RegistrationResponse("User not Registered"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<RegistrationResponse> reSendOtp(@RequestBody OtpVerificationRequest request){
        return new ResponseEntity<>(emailOtpService.reSendOtp(request.getUserId()), HttpStatus.OK);
    }

}
