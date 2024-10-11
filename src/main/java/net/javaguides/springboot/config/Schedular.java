package net.javaguides.springboot.config;

import lombok.extern.slf4j.Slf4j;
import net.javaguides.springboot.model.EmailOtp;
import net.javaguides.springboot.service.EmailOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  this method runs once in a day it will delete the expired rows in EmailOtp table
 */
@Slf4j
@Component
public class Schedular {

    @Autowired
    private EmailOtpService emailOtpService;
    @Scheduled(cron ="0 0 0 * * *") //run once in everyday
    public void deleteExpiredRowsInEmailOtpTable(){
        log.info("deleting expired rows in EmailOtp table  ");
        List<EmailOtp> expiredRows =  emailOtpService.findAll().stream().filter(emailOtp -> emailOtp.getExpirationTime().isBefore(LocalDateTime.now())).toList();
        emailOtpService.delete(expiredRows);
        log.info("succesully deleted expired rows in EmailOtp table");
    }
}
