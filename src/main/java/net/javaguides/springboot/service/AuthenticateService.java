package net.javaguides.springboot.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import net.javaguides.springboot.BetForeCastUtil.OtpUtil;
import net.javaguides.springboot.dto.ProfileDto;
import net.javaguides.springboot.dto.UpdateUserRequest;
import net.javaguides.springboot.exception.UserAlreadyExistsException;
import net.javaguides.springboot.model.EmailOtp;
import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.request.AuthenticationRequest;
import net.javaguides.springboot.request.RegisterRequest;
import net.javaguides.springboot.response.AuthenticationResponse;
import net.javaguides.springboot.response.RegistrationResponse;
import org.apache.hc.client5.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

//    public RegistrationResponse register(RegisterRequest request) {
//        // Check if the user already exists
//        if (repository.existsByEmail(request.getEmail())) {
//            throw new UserAlreadyExistsException("User with this email already exists");
//        }
//
//        // Create new user
//        var user = User.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .mobile(request.getMobile())
//                .role(Role.USER)
//                .build();
//        String otp = OtpUtil.generateOTP();
//        try{
//            emailBody = otp;
//            emailSubject = "Your Betforecast OTP valid for 5 minutes";
//            emailService.sendEmail(emailBody, user.getEmail(),emailSubject);
//            emailOtpService.saveEmailOtp(user.getEmail(),otp);
//        }
//        catch(Exception e){
//            System.out.println("email not sent to "+ user.getEmail() );
//        }
//
//        System.out.println(user);
//
//        // Save user to the database
//        repository.save(user);
//        return RegistrationResponse.builder()
//                .message("User registered successfully")
//                .build();
//    }

    public User findByUserId(String userId){
        return repository.findByEmail(userId).orElse(new User());
    }
    public List<User> getAll(){
        return repository.findAll();
    }

    public User findByMobile(String mobile){
        return repository.findByMobile(mobile).orElse(new User());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws InvalidCredentialsException {
        // Authenticate the user with provided credentials
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Retrieve the user from the repository
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Generate JWT token for authenticated user
        var jwtToken = jwtService.generateToken(user);

        // Return authentication response with token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userID(user.getEmail())
                .role(user.getRole().name())
                .build();
    }



    public RegistrationResponse register(EmailOtp emailOtp) {
        // Create new user
        var user = User.builder()
                .firstName(emailOtp.getFirstName())
                .lastName(emailOtp.getLastName())
                .email(emailOtp.getUserId())
                .password(emailOtp.getPassword())
                .mobile(emailOtp.getMobile())
                .role(Role.USER)
                .build();
        System.out.println(user);

        // Save user to the database
        repository.save(user);
        return RegistrationResponse.builder()
                .message("User registered successfully")
                .build();
    }


    public void save(User user) {
        repository.save(user);
    }

}

//package net.javaguides.springboot.service;
//
//import lombok.RequiredArgsConstructor;
//import net.javaguides.springboot.model.Role;
//import net.javaguides.springboot.model.User;
//import net.javaguides.springboot.repository.UserRepository;
//import net.javaguides.springboot.request.AuthenticationRequest;
//import net.javaguides.springboot.request.RegisterRequest;
//import net.javaguides.springboot.response.AuthenticationResponse;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticateService {
//
//    private final UserRepository repository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//
//    public AuthenticationResponse register(RegisterRequest request) {
//      var user= User.builder()
//              .firstName(request.getFirstname())
//              .lastName(request.getLastname())
//              .email(request.getEmail())
//              .password(passwordEncoder.encode(request.getPassword()))
//              .role((Role.USER))
//              .build();
//      repository.save(user);
//      var jwtToken=jwtService.generateToken(user);
//      return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
//
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        var user = repository.findByEmail(request.getEmail())
//                .orElseThrow();
//        var jwtToken=jwtService.generateToken(user);
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
//}
