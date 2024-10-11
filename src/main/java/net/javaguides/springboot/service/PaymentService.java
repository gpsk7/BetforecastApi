package net.javaguides.springboot.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
//import net.javaguides.springboot.model.PaymentConfigurations;
import net.javaguides.springboot.model.PaymentConfigurations;
import net.javaguides.springboot.model.TransactionDetails;

import net.javaguides.springboot.repository.PaymentConfigurationsRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

//    public static final String KEY="rzp_test_I94FrvS0yaSqBQ";
//    public static final String KEY_SECRET="OjBGEFBG3hiFpiQKkIYNkXKF";
    public static final String CURRENCY="INR";

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PaymentConfigurationsService paymentConfigurationsService;

    public TransactionDetails createTransaction(Double amount,String userId){

        try{
            PaymentConfigurations paymentConfigurations = paymentConfigurationsService.getConfigKeys();
            if (paymentConfigurations == null) {
                throw new RuntimeException("Payment configuration not found");
            }
            String key = paymentConfigurations.getApiKey();
            String keySecret = paymentConfigurations.getSecretKey();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount",(amount * 100));
            jsonObject.put("currency", CURRENCY);
            jsonObject.put("receipt", "rcptId_"+ (int)(Math.random()*10000000));
            RazorpayClient razorpayClient = new RazorpayClient(key,keySecret);
            Order order = razorpayClient.orders.create(jsonObject);
            System.out.println(order);
            TransactionDetails transactionDetails = prepareTransactionDetails(order,userId);
            transactionService.save(transactionDetails) ;
            return transactionDetails;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    private TransactionDetails prepareTransactionDetails(Order order,String userId){
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");
        String receiptId =order.get("receipt");
        String status = order.get("status");

     return TransactionDetails.builder().amount(amount).currency(currency).orderId(orderId)
             .userId(userId).receiptId(receiptId).status(status).build();
    }
}
