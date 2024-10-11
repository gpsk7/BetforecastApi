package net.javaguides.springboot.controller;

import net.javaguides.springboot.WebRequest.TransactionRequest;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.Feedback;
import net.javaguides.springboot.model.PaymentDetails;
import net.javaguides.springboot.model.TransactionDetails;
import net.javaguides.springboot.service.FeedbackService;
import net.javaguides.springboot.service.PaymentDetailsService;
import net.javaguides.springboot.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/races")
public class PaymentController {
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @PostMapping("/transaction")
    public TransactionDetails createTransaction(@RequestBody TransactionRequest transactionRequest){
        return paymentService.createTransaction(transactionRequest.getAmount(),transactionRequest.getUserId());
    }

    @PostMapping("/savePayment")
    public ResponseEntity<String> savePayment(@RequestBody PaymentDetails paymentDetails){
        PaymentDetails savedDetails = paymentDetailsService.save(paymentDetails);
        if(savedDetails != null){
            return ResponseEntity.ok("Payment Details Saved");
        }
        else {
            throw new ApiException("payment details not saved", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllPayments")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PaymentDetails> getAllPayments(){
        return paymentDetailsService.getAllPayments();
    }
  @PostMapping("/feedback")
    public ResponseEntity<Feedback> saveFeedback(@RequestBody Feedback feedback){
        Feedback feedbackDetails = feedbackService.save(feedback);
        return ResponseEntity.ok(feedbackDetails);
  }

    @GetMapping("/getUserPayments/{userID}")
    public List<PaymentDetails> getUserPayments(@PathVariable String userID){
        return paymentDetailsService.getUserPayments(userID);
    }

}
