package net.javaguides.springboot.controller;


import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.Amount;
import net.javaguides.springboot.repository.AmountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/amount")
public class AmountController {

    @Autowired
    private AmountRepository amountRepository;

    @PostMapping()
    public ResponseEntity<String> createAmount(@RequestBody Amount amount){
       Optional<Amount> updateAmount = amountRepository.findById(1L);
       if(updateAmount.isEmpty()){
            amountRepository.save(amount);
           return ResponseEntity.ok("amount saved succesfully");
       }
        updateAmount.get().setValue(amount.getValue());
        amountRepository.save(updateAmount.get());
        return ResponseEntity.ok("amount saved succesfully");
    }

    @GetMapping
    public ResponseEntity<Amount> get(){
        Optional<Amount> amountOpt = amountRepository.findById(1L);
        if (amountOpt.isEmpty()) {
            throw new ApiException("No data found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(amountOpt.get());
    }
}
