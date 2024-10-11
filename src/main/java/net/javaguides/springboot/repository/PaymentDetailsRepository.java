package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

    PaymentDetails findByPaymentId(String paymentId);

    List<PaymentDetails> findByUserId(String userId);

}
