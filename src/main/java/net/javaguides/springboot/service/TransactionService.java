package net.javaguides.springboot.service;

import net.javaguides.springboot.model.TransactionDetails;
import net.javaguides.springboot.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public void save(TransactionDetails transactionDetails) {
        transactionRepository.save(transactionDetails);
    }
}
