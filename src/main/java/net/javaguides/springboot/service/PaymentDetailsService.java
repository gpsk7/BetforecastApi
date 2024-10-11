package net.javaguides.springboot.service;

import jakarta.transaction.Transactional;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.PaymentDetails;
import net.javaguides.springboot.repository.PaymentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentDetailsService {
    private final String paymentStatus = "payment_success";
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private UserSubscriptionService userSubscriptionService;
    @Autowired
    private EmailService emailService;


    @Transactional
    public PaymentDetails save(PaymentDetails paymentDetails){
        PaymentDetails savedPaymentDetails =  paymentDetailsRepository.save(paymentDetails);
        if(savedPaymentDetails.getStatus().equals(paymentStatus)){
            userSubscriptionService.saveSubscriptionDetails(savedPaymentDetails.getUserId(),savedPaymentDetails.getCreationTime());
        }
        sendEmail(paymentDetails);
        return savedPaymentDetails;
    }

    private void sendEmail(PaymentDetails paymentDetails) {
        try{
            String emailBody ="Below are the details of your payment:\n\n" +
                    "Order ID: " + paymentDetails.getOrderId()+ "\n" +
                    "Date: " + paymentDetails.getCreationTime() + "\n" +
                    "Amount: " + (paymentDetails.getAmount()/100)+" "+paymentDetails.getCurrency()  + "\n" +
                    "Description: " + paymentDetails.getStatus() + "\n\n" +
                    "If you have any questions or need further assistance, please do not hesitate to contact our support team.\n\n" +
                    "Thank you for choosing Betforecast.\n\n" +
                    "Best regards,\n" +
                    "Betforecast\n" +
                    "support@betforecast.com\n" +
                    "123456789";
            String emailSubject = "Betforcast "+ paymentDetails.getStatus();
            emailService.sendEmail(emailBody, paymentDetails.getUserId(), emailSubject);
        }
        catch(Exception e){
            throw new ApiException("email not sent to "+ paymentDetails.getUserId() , HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }

    public List<PaymentDetails> getAllPayments(){
        return paymentDetailsRepository.findAll();
    }

    public List<PaymentDetails> getUserPayments(String userId){
        return paymentDetailsRepository.findByUserId(userId);
    }
}
