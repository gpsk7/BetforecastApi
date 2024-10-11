package net.javaguides.springboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 *  the user details are stored in this "EmailOtp" table when he/she register or use forgot password,
 *  otp is generated and stored with is email,
 *  once the email is verified then the user is moved to user table,
 *  that record is removed from this EmailOtp table
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class EmailOtp {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "email_Id",unique = true)
    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    @Column(name = "mobile", unique = true)
    private String mobile;
    private String otp;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime expirationTime;
    private boolean isVerified;

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

}
