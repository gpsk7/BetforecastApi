package net.javaguides.springboot.service;

import net.javaguides.springboot.model.PaymentConfigurations;
import net.javaguides.springboot.repository.PaymentConfigurationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentConfigurationsService {
    @Autowired
    private PaymentConfigurationsRepository paymentConfigurationsRepository;

    public PaymentConfigurations getConfigKeys(){
        return paymentConfigurationsRepository.findFirstByOrderByIdAsc();

    }
}
