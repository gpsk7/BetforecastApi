package net.javaguides.springboot.BetForeCastUtil;

import java.util.Random;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OtpUtil {
        private static final int EXPIRATION_TIME = 5; // OTP expiration time in minutes
        private static final int OTP_LENGTH = 6; // Length of the OTP
        public static String generateOTP() {
            Random random = new Random();
            return String.format("%06d", random.nextInt(1000000));
        }

//    public boolean verifyOTP(String key, String otp) {
//        if (!otpData.containsKey(key)) {
//            return false;
//        }
//        if (System.currentTimeMillis() > otpExpirationTime.get(key)) {
//            otpData.remove(key);
//            otpExpirationTime.remove(key);
//            return false;
//        }
//        return otp.equals(otpData.get(key));
//    }
    }


