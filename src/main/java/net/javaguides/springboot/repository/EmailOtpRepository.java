package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.EmailOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailOtpRepository extends JpaRepository<EmailOtp,Integer> {
    Optional<EmailOtp> findByUserId(String userId);
    Optional<EmailOtp> findByUserIdAndOtp(String userId, String otp);
}
