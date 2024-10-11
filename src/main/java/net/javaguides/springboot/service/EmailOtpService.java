package net.javaguides.springboot.service;

import lombok.RequiredArgsConstructor;
import net.javaguides.springboot.BetForeCastUtil.OtpUtil;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.exception.DuplicateEmailException;
import net.javaguides.springboot.exception.DuplicateMobileNumberException;
import net.javaguides.springboot.model.EmailOtp;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.EmailOtpRepository;
import net.javaguides.springboot.request.RegisterRequest;
import net.javaguides.springboot.response.RegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmailOtpService {

    @Autowired
    private EmailOtpRepository emailOtpRepository;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    public RegistrationResponse register(RegisterRequest request) {
         User user = authenticateService.findByUserId(request.getEmail());
           if(user.getId() != null){
           throw new DuplicateEmailException("Email already exists: " + request.getEmail());
           }

        User userExistWithMobile= authenticateService.findByMobile(request.getMobile());
        if(userExistWithMobile.getMobile()!=null){
            throw new DuplicateMobileNumberException("Mobile number already exists: " + request.getMobile());
        }


        String otp = OtpUtil.generateOTP();
        try{
            String emailBody ="Dear "+ request.getFirstName()+" "+request.getLastName() +"\n\nWe have received a request to complete your Registration process. To proceed, please use the following One-Time Password (OTP):\n\n" +
                    otp + "\n\nThis OTP is valid for 5 minutes and can be used only once. If you did not request this OTP, please ignore this email or contact our support team immediately.\n\n" +
                    "Thank you for your attention.\n\nBest regards,\n Betforecast \n support@betforecast.com \n 123456789";
            String emailSubject = "Your Betforecast OTP valid for 5 minutes";
            emailService.sendEmail(emailBody, request.getEmail(), emailSubject);
        }
        catch(Exception e){
            throw new ApiException("email not sent to "+ request.getEmail() ,HttpStatus.INTERNAL_SERVER_ERROR );
        }

        EmailOtp userDetails = emailOtpRepository.findByUserId(request.getEmail()).orElse(new EmailOtp()); //
        if(userDetails.getId() != null){
            userDetails.setOtp(otp);
            userDetails.setMobile(request.getMobile());
            userDetails.setLastName(request.getLastName());
            userDetails.setFirstName(request.getFirstName());
            userDetails.setPassword(passwordEncoder.encode(request.getPassword()));
            userDetails.setExpirationTime(LocalDateTime.now().plusMinutes(5L));
            emailOtpRepository.save(userDetails);
            return RegistrationResponse.builder().message("please verify the email").build();
        }

        EmailOtp emailOtp =  EmailOtp.builder()
                .userId(request.getEmail())
                .expirationTime(LocalDateTime.now().plusMinutes(5L))
                .otp(otp)
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .mobile(request.getMobile())
                .build();
        emailOtpRepository.save(emailOtp);
        return RegistrationResponse.builder().message("please verify the email").build();
    }

    private boolean userAlreadyExist(String userId) {
       return emailOtpRepository.findByUserId(userId).isEmpty();
    }

    public boolean verifyOTP(String userId, String otp){
        EmailOtp emailOtp = emailOtpRepository.findByUserId(userId).orElse(new EmailOtp());
        if(emailOtp.getId() == null){
            throw new ApiException("No user found with email",HttpStatus.NOT_FOUND);
        }
        if(emailOtp.getExpirationTime().isAfter(LocalDateTime.now())){
            if(emailOtp.getOtp().equals(otp)){
                authenticateService.register(emailOtp);
                emailOtpRepository.delete(emailOtp);
                registrationSuccessfullEmail(emailOtp);
                return Boolean.TRUE;
            }
            else{
                throw new ApiException("OTP not valid",HttpStatus.BAD_REQUEST);
            }
        }
        else{
            emailOtpRepository.delete(emailOtp);
            throw new ApiException("OTP validity Expired",HttpStatus.BAD_REQUEST);
        }
    }
    public RegistrationResponse reSendOtp(String userId) {
        EmailOtp emailOtp = emailOtpRepository.findByUserId(userId).orElse(new EmailOtp());
        if(emailOtp.getId() == null){
            throw new ApiException("No user found with email",HttpStatus.NOT_FOUND);
        }
        String otp = OtpUtil.generateOTP();
        try{
            String emailBody ="\n\n We have received a request to Resend your OTP for Registration process. To proceed, please use the following One-Time Password (OTP):\n\n" +
                    otp + "\n\nThis OTP is valid for 5 minutes and can be used only once. If you did not request this OTP, please ignore this email or contact our support team immediately.\n\n" +
                    "Thank you for your attention.\n\nBest regards,\n Betforecast \n support@betforecast.com \n 123456789";
            String emailSubject = "Betforecast Resend-OTP valid for 5 minutes";
            emailService.sendEmail(emailBody,userId, emailSubject);
        }
        catch(Exception e){
           throw new ApiException("email not sent to "+ userId,HttpStatus.INTERNAL_SERVER_ERROR );
        }
        emailOtp.setOtp(otp);
        emailOtp.setExpirationTime(LocalDateTime.now().plusMinutes(5L));
        emailOtpRepository.save(emailOtp);
        return new RegistrationResponse("OTP Sent succesfully");
    }

    public void registrationSuccessfullEmail(EmailOtp emailOtp){
        try{
            String emailBody = "Dear "+emailOtp.getFirstName()+" "+ emailOtp.getLastName()+",\n\n" +
                    "Congratulations! Your registration with Betforecast has been successfully completed. Below are your registration details:\n\n" +
                    "Username: "+emailOtp.getUserId()+"\n\n" +
                    "You can now log in to your account and start using our services.\n\n" +
                    "If you have any questions or need further assistance, please do not hesitate to contact our support team.\n\n" +
                    "Thank you for registering with Betforecast. We look forward to serving you.\n\n" +
                    "Best regards,\n" +
                    "Betforecast\n" +
                    "support@betforecast.com\n" +
                    "12315447787";
            String emailSubject = "Congratulation!! Registration Succesfull";
            emailService.sendEmail(emailBody,emailOtp.getUserId(), emailSubject);
        }
        catch(Exception e){
            throw new ApiException("email not sent to "+ emailOtp.getUserId(),HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }

    public List<EmailOtp> findAll(){
       return emailOtpRepository.findAll();
    }

    public void delete(List<EmailOtp> emailOtps){
        emailOtpRepository.deleteAll(emailOtps);
    }
}
