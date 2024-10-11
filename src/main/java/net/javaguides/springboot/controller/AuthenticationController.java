package net.javaguides.springboot.controller;

import lombok.RequiredArgsConstructor;
import net.javaguides.springboot.dto.UpdateUserRequest;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.request.AuthenticationRequest;
import net.javaguides.springboot.request.RegisterRequest;
import net.javaguides.springboot.response.AuthenticationResponse;
import net.javaguides.springboot.response.RegistrationResponse;
import net.javaguides.springboot.service.AuthenticateService;
import org.apache.hc.client5.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticateService service;

//    @PostMapping("/register")
//    public ResponseEntity<RegistrationResponse> register(@RequestBody RegisterRequest request){
//        RegistrationResponse message = service.register(request);
//        return ResponseEntity.ok(message);
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws InvalidCredentialsException {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
