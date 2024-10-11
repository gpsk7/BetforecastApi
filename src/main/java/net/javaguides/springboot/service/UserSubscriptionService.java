package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.UserSubscription;
import net.javaguides.springboot.repository.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserSubscriptionService {
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;


    public void saveSubscriptionDetails(String userId, LocalDateTime creationTime) {
        LocalDateTime expirationDate = creationTime.plusDays(1L);
        UserSubscription userSubscription = UserSubscription.builder().userId(userId)
                .validFrom(creationTime).ExpirationDate(expirationDate).build();
        userSubscriptionRepository.save(userSubscription);
    }

    public boolean isUserPaymentDone(String userId) {
        UserSubscription userSubscription = userSubscriptionRepository
                .findTopByUserIdOrderByValidFromDesc(userId)
                .orElseThrow(() -> new ApiException("User subscription not found", HttpStatus.NOT_FOUND));
        return userSubscription.getExpirationDate().isAfter(LocalDateTime.now());
    }

}

