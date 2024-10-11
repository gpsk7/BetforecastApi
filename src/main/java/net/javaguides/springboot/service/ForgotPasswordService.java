package net.javaguides.springboot.service;

import jakarta.mail.MessagingException;
import net.javaguides.springboot.BetForeCastUtil.OtpUtil;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.EmailOtp;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.EmailOtpRepository;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.request.ChangePasswordRequest;
import net.javaguides.springboot.request.ForgotPasswordRequest;
import net.javaguides.springboot.request.OtpVerificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailOtpRepository emailOtpRepository;

    @Autowired
    private PasswordEncoder encodePassword;

    public void forgotPassword(ForgotPasswordRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getUserId());
        if (existingUser.isEmpty()) {
            throw new ApiException("Email not found", HttpStatus.NOT_FOUND);
        }
        String otp = OtpUtil.generateOTP();
        try {
            String emailBody = "Dear " + existingUser.get().getFirstName() + " " + existingUser.get().getLastName() + "\n\nWe have received a request to change the password. To proceed, please use the following One-Time Password (OTP):\n\n" +
                    otp + "\n\nThis OTP is valid for 5 minutes and can be used only once. If you did not request this OTP, please ignore this email or contact our support team immediately.\n\n" +
                    "Thank you for your attention.\n\nBest regards,\n Betforecast \n support@betforecast.com \n 123456789";
            String emailSubject = "Your Betforecast OTP valid for 5 minutes";
            emailService.sendEmail(emailBody, request.getUserId(), emailSubject);

        } catch (Exception e) {
            throw new ApiException("email not sent to " + request.getUserId(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Optional<EmailOtp> existingUserInEmail=emailOtpRepository.findByUserId(request.getUserId());
        if(existingUserInEmail.isPresent()){
            existingUserInEmail.get().setOtp(otp);
            existingUserInEmail.get().setVerified(Boolean.FALSE);
            existingUserInEmail.get().setExpirationTime(LocalDateTime.now().plusMinutes(5));
            emailOtpRepository.save(existingUserInEmail.get());
            return;
        }
        EmailOtp emailOtp = new EmailOtp();
        emailOtp.setOtp(otp);
        emailOtp.setUserId(request.getUserId());
        emailOtp.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        emailOtp.setVerified(Boolean.FALSE); // Ensure the new OTP is not verified

        emailOtpRepository.save(emailOtp);

    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public boolean verifyOtp(OtpVerificationRequest request) {
        EmailOtp emailOtp = emailOtpRepository.findByUserIdAndOtp(request.getUserId(), request.getOtp())
                .orElseThrow(() -> new ApiException("Invalid or expired OTP", HttpStatus.BAD_REQUEST));

        if (emailOtp.getExpirationTime().isBefore(LocalDateTime.now())) {
            emailOtpRepository.delete(emailOtp);
            throw new ApiException("OTP has expired", HttpStatus.BAD_REQUEST);
        }

        if (emailOtp.isVerified()) {
            throw new ApiException("OTP already verified", HttpStatus.BAD_REQUEST);
        }

        // OTP is valid
        emailOtp.setVerified(true);
        emailOtpRepository.save(emailOtp);

        return true;
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getUserId())
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        // Ensure OTP verification status
        EmailOtp emailOtp = emailOtpRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ApiException("OTP not found", HttpStatus.NOT_FOUND));

        if (!emailOtp.isVerified()) {
            throw new ApiException("OTP not verified", HttpStatus.BAD_REQUEST);
        }

        if (!request.getNewPassword().equals(request.getRepeatPassword())) {
            throw new ApiException("Passwords do not match", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(encodePassword.encode(request.getNewPassword())); // Ensure to encode the password
        userRepository.save(user);

        // After password change, reset the OTP status or delete it if needed
        emailOtpRepository.delete(emailOtp);
    }

    // Resend OTP implementation
    public void resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found with email", HttpStatus.NOT_FOUND));

        EmailOtp existingOtp = emailOtpRepository.findByUserId(email)
                .orElseThrow(() -> new ApiException("No OTP record found for email", HttpStatus.NOT_FOUND));

        // Generate a new OTP
        String newOtp = generateOtp();
        existingOtp.setOtp(newOtp);
        existingOtp.setExpirationTime(LocalDateTime.now().plusMinutes(5)); // Reset expiration time
        existingOtp.setVerified(false); // Ensure the new OTP is not verified

        // Save the updated OTP record
        emailOtpRepository.save(existingOtp);

        // Send the new OTP via email
        try {
            String emailBody = "Dear " + user.getFirstName() + " " + user.getLastName() + "\n\nWe have received a request to resend your OTP. Please use the following One-Time Password (OTP):\n\n" +
                    newOtp + "\n\nThis OTP is valid for 5 minutes and can be used only once. If you did not request this OTP, please ignore this email or contact our support team immediately.\n\n" +
                    "Thank you for your attention.\n\nBest regards,\n Betforecast \n support@betforecast.com \n 123456789";
            String emailSubject = "Your Betforecast OTP valid for 5 minutes";
            emailService.sendEmail(emailBody, email, emailSubject);

        } catch (Exception e) {
            throw new ApiException("email not sent to " + email, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}