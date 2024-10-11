package net.javaguides.springboot.controller;

import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.request.ChangePasswordRequest;
import net.javaguides.springboot.request.ForgotPasswordRequest;
import net.javaguides.springboot.request.OtpVerificationRequest;
import net.javaguides.springboot.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/forgotPassword")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request){
        try{
            forgotPasswordService.forgotPassword(request);
            return ResponseEntity.ok("OTP sent to your email");
        }
        catch (ApiException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        try {
            if (forgotPasswordService.verifyOtp(request)) {
                return ResponseEntity.ok("OTP verified successfully");
            } else {
                // This line should not be reached because verifyOtp() either succeeds or throws an exception.
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP verification failed");
            }
        } catch (ApiException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            forgotPasswordService.changePassword(request);
            return ResponseEntity.ok("Password changed successfully");
        } catch (ApiException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody ForgotPasswordRequest request) {
        try {
            forgotPasswordService.resendOtp(request.getUserId());
            return ResponseEntity.ok("OTP resent to your email");
        } catch (ApiException ex) {
            return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
        }
    }
}
